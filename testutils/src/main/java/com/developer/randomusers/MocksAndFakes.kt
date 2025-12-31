package com.developer.randomusers

import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.network.model.IdHttpResponse
import com.developer.randomusers.network.model.NameHttpResponse
import com.developer.randomusers.network.model.ResultsAndInfoHttpResponse
import com.developer.randomusers.network.model.UserHttpResponse
import com.developer.randomusers.network.model.toUserEntity
import com.developer.randomusers.repository.MqttEventRepository
import kotlinx.coroutines.flow.emptyFlow


val fakeNameHttpResponse = NameHttpResponse(
    title = "Mrs.",
    first = "Fake",
    last = "Name"
)

val fakeIdHttpResponse = IdHttpResponse(
    name = "fakeNameAttributeForIdHttpResponse1",
    value = "fakeNameValueForIdHttpResponse1"
)

val fakeIdHttpResponseWithEmptyName = IdHttpResponse(
    name = "",
    value = "fakeIdHttpResponseWithEmptyName"
)

val fakeUserHttpResponse = UserHttpResponse(
    gender = "female",
    nameHttpResponse = fakeNameHttpResponse,
    locationHttpResponse = null,
    email = "fakename@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 0100",
    cell = "+1 555 555 0100",
    id = fakeIdHttpResponse,
    pictureHttpResponse = null,
    nat = null
)

val fakeUserHttpResponseWithIdWithEmptyName = UserHttpResponse(
    gender = "male",
    nameHttpResponse = fakeNameHttpResponse,
    locationHttpResponse = null,
    email = "fakename2@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 2342",
    cell = "+1 555 555 3245",
    id = fakeIdHttpResponseWithEmptyName,
    pictureHttpResponse = null,
    nat = null
)

val fakeUserEntity = fakeUserHttpResponse.toUserEntity()

val fakeUser = fakeUserHttpResponse.toUserEntity().toUser()

val fakeNonEmptyNetworkResults = ResultsAndInfoHttpResponse(
    userHttpResponses = arrayListOf(fakeUserHttpResponse)
)

val fakeNonEmptyResponseRESTClient = InMemoryRESTClient(
    expectedResults =
        fakeNonEmptyNetworkResults.userHttpResponses
)

val fakeEmptyMqttClientManager = FakeMqttClientManager(emptyFlow())

val dummyMqttEventRepository = MqttEventRepository(fakeEmptyMqttClientManager)