package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.router.ASSIGN_ROLE_PATH
import com.samsung.healthcare.account.adapter.web.router.AssignRoleRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    AssignRoleHandler::class,
    AssignRoleRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtTokenAuthenticationFilter::class,
    SecurityConfig::class,
)
internal class AssignRoleHandlerTest {
    @MockkBean
    private lateinit var accountService: AccountService

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `should return ok`() {
        every { accountService.assignRoles(any(), any()) } returns Mono.empty()

        webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("test-account", listOf(Researcher("project-id").roleName)),
        )
    }

    @Test
    fun `should return bad request when account id is null`() {
        webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest(null, listOf(Researcher("project-id").roleName)),
        )
    }

    @Test
    fun `should return bad request when roles is null`() {
        webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("account-id", null),
        )
    }

    data class TestRequest(val accountId: String?, val roles: List<String>? = emptyList())
}
