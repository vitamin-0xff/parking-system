package com.parking.management.comman

import com.parking.management.comman.models.*
import com.parking.management.ed.events.ErrorsEventDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalWrappe(
    val errorDispatcher: ErrorsEventDispatcher
) : ResponseEntityExceptionHandler(), ResponseBodyAdvice<Any> {

    // List of paths you want to EXCLUDE (even if they match included)
    private val excludedPatterns = listOf(
        "/actuator/", "/swagger-ui/", "/v3/api-docs", "/webjars/", "/error", "/docs"
    )

    // ==================== Response Wrapping ====================
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        // 1. Never double-wrap
        if (returnType.parameterType.kotlin == ApiResponse::class) {
            return false
        }

        // 2. Never wrap raw strings (actuator, error pages, etc.)
        if (returnType.parameterType == String::class.java) {
            return false
        }

        // 3. Get current request safely
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)
            ?.request
            ?: return false

        val path = request.requestURI

        // 4. Exclude has priority
        if (excludedPatterns.any { path.startsWith(it) }) {
            return false
        }

        // 5. Only include matching API paths
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val servletRequest = (request as? ServletServerHttpRequest)?.servletRequest ?: return body
        val path = servletRequest.requestURI

        if (body is ResponseEntity<*>) {
            if (body.body is ApiResponse<*>) return body
            return ApiResponse.success(body.body, body.statusCode.value(), path)
        }

        /**
         * if it is an API response, return it as is
         */
        if(body is ApiResponse<*>) return body

        return ApiResponse.success(body, 200, path)
    }

    // ==================== Exception Handling ====================
    private fun HttpServletRequest.path() = this.requestURI

    private fun error(code: String, message: String, status: Int, path: String, details: List<String>? = null) =
        ResponseEntity.status(200)
            .body(ApiResponse.error(ApiError(code, message, details), status, path))

    /**
     * replaced by NotFoundException
     */
    @ExceptionHandler(NotFoundException::class)
    fun handleRNF(e: NotFoundException, req: HttpServletRequest) =
        error("RESOURCE_NOT_FOUND", e.message!!, 404, req.path())

    @ExceptionHandler(PaymentRequiredException::class)
    fun handleRNF(e: PaymentRequiredException, req: HttpServletRequest) =
        error(e.errorCode, e.message!!, e.status, req.path())

    @ExceptionHandler(BadRequestException::class)
    fun handle(e: BadRequestException, req: HttpServletRequest) =
        error(e.errorCode, e.message!!, 400, req.path())

    @ExceptionHandler(UnauthorizedException::class)
    fun handle(e: UnauthorizedException, req: HttpServletRequest) =
        error(e.errorCode, e.message!!, 401, req.path())

    @ExceptionHandler(ForbiddenException::class)
    fun handle(e: ForbiddenException, req: HttpServletRequest) =
        error(e.errorCode, e.message!!, 403, req.path())

    @ExceptionHandler(ConflictException::class)
    fun handle(e: ConflictException, req: HttpServletRequest) =
        error(e.errorCode, e.message!!, 409, req.path())

    @ExceptionHandler(UnprocessableEntityException::class)
    fun handle(e: UnprocessableEntityException, req: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        val details = e.errors?.map { "${it.key}: ${it.value}" }
        return error(e.errorCode, e.message!!, 422, req.path(), details)
    }

    @ExceptionHandler(ThirdPartyServiceException::class)
    fun handle(e: ThirdPartyServiceException, req: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        return error(e.errorCode, e.message!!, e.status, req.path())
    }


    // Validation errors
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors = ex.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(200)
            .body(ApiResponse.error(ApiError("VALIDATION_FAILED", "Validation failed", errors), 400, (request as ServletWebRequest).request.requestURI))
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return ResponseEntity.status(200)
            .body(ApiResponse.error(ApiError("BAD_REQUEST", "Invalid JSON format Malformed request"), 400, (request as ServletWebRequest).request.requestURI))

    }

    // Fallback
    @ExceptionHandler(Exception::class)
    fun handleAll(e: Exception, req: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        errorDispatcher.pushErrorEvent(e)
        return error("INTERNAL_ERROR", "Something went wrong", 500, req.path())
    }

}