package com.developer.randomusers.ui.screens.previewutils

import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.model.UsersState
import com.developer.randomusers.network.model.IdHttpResponse
import com.developer.randomusers.network.model.NameHttpResponse
import com.developer.randomusers.network.model.UserHttpResponse
import com.developer.randomusers.network.model.toUserEntity

val previewUserNameHttpResponse1 = NameHttpResponse(
    title = "Mr.",
    first = "Preview1",
    last = "Name1"
)

val previewUserIdHttpResponse1 = IdHttpResponse(
    name = "previewUserNameAttributeForIdHttpResponse1",
    value = "previewUserNameValueForIdHttpResponse1"
)

val previewUserHttpResponse1 = UserHttpResponse(
    gender = "male",
    nameHttpResponse = previewUserNameHttpResponse1,
    locationHttpResponse = null,
    email = "previewusername1@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 0002",
    cell = "+1 555 555 0002",
    id = previewUserIdHttpResponse1,
    pictureHttpResponse = null,
    nat = null
)

val previewUserNameHttpResponse2 = NameHttpResponse(
    title = "Mrs.",
    first = "Preview2",
    last = "Name2"
)

val previewUserIdHttpResponse2 = IdHttpResponse(
    name = "previewUserNameAttributeForIdHttpResponse2",
    value = "previewUserNameValueForIdHttpResponse2"
)

val previewUserHttpResponse2 = UserHttpResponse(
    gender = "female",
    nameHttpResponse = previewUserNameHttpResponse2,
    locationHttpResponse = null,
    email = "previewusername2@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 0002",
    cell = "+1 555 555 0002",
    id = previewUserIdHttpResponse2,
    pictureHttpResponse = null,
    nat = null
)

val previewUser1 = previewUserHttpResponse1.toUserEntity().toUser()
val previewUser2 = previewUserHttpResponse2.toUserEntity().toUser()

val previewUserState = UsersState(users = listOf(previewUser1, previewUser2))

