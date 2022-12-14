package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.router.RESET_PASSWORD_PATH
import com.samsung.healthcare.account.adapter.web.router.ResetAccountRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.port.input.ResetPasswordCommand
import com.samsung.healthcare.account.application.port.input.ResetPasswordUseCase
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileUseCase
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.UUID

@WebFluxTest
@Import(
    ResetAccountHandler::class,
    ResetAccountRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtTokenAuthenticationFilter::class,
    SecurityConfig::class,
)
internal class ResetAccountHandlerTest {

    @MockkBean
    private lateinit var resetPasswordService: ResetPasswordUseCase

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @MockkBean
    private lateinit var updateAccountProfileService: UpdateAccountProfileUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `should return ok`() {
        val resetRequest = ResetRequest("reset-token", "new-pw", emptyMap())
        every {
            resetPasswordService.resetPassword(
                ResetPasswordCommand(
                    resetRequest.resetToken!!,
                    resetRequest.password!!
                )
            )
        } returns Mono.just(UUID.randomUUID().toString())

        webClient.post(RESET_PASSWORD_PATH, resetRequest)
            .expectStatus()
            .isOk
    }

    @Test
    fun `should return bad request when reset token is not given`() {
        webClient.post(RESET_PASSWORD_PATH, ResetRequest(null, "new-pw"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when password is not given`() {
        webClient.post(RESET_PASSWORD_PATH, ResetRequest("token", null))
            .expectStatus()
            .isBadRequest
    }

    private data class ResetRequest(
        val resetToken: String?,
        val password: String?,
        val profile: Map<String, Any>? = null
    )
}
