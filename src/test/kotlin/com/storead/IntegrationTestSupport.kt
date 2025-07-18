package com.storead

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
abstract class IntegrationTestSupport(
    body: BehaviorSpec.() -> Unit = {}
) : BehaviorSpec(body)