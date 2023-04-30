package com.zerodstocking.feishuchatgpt.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*


fun jsonMapper(): ObjectMapper = jacksonObjectMapper()
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)