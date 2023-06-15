package com.pixel_Alireza.routing.auth

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.request.AuthenticationReq
import com.pixel_Alireza.data.model.request.UpdatePassword
import com.pixel_Alireza.data.model.request.UpdateUsername
import com.pixel_Alireza.data.model.response.auth.SecretInfo
import com.pixel_Alireza.data.model.response.auth.SignInResponse
import com.pixel_Alireza.data.model.response.auth.SignUpResponse
import com.pixel_Alireza.data.model.user.User
import com.pixel_Alireza.data.model.user.UserDataManager
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.hashing.SaltedHash
import com.pixel_Alireza.security.token.TokenClaim
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.signUp(
    hashingService: HashingService,
    userDataManager: UserDataManager
) {

    post("signUp") {

        val req = call.receive<AuthenticationReq>()
        var username = ""


        if (req.equals(null)) {
            call.respond(HttpStatusCode.BadRequest, "Bad Request")
            return@post
        }

        if (req.username == null) {
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Fields blank or short pass exception"))
            return@post
        } else {
            username = req.username
        }

        //<editor-fold desc="isFormatCorrect?">
        val isFieldsBlank = req.password.isBlank() || username.isBlank()
        val isPasswordTooShort = req.password.length < 8
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!req.email.matches(emailPattern.toRegex())) {
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Invalid email format"))
            return@post
        }
        if (isFieldsBlank || isPasswordTooShort) {
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Invalid information"))
            return@post
        }
        //</editor-fold>

        val saltedHash = hashingService.generateSaltedHash(req.password)
        val user = User(
            username = username,
            email = req.email,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val res = userDataManager.insertNewUser(user)
        when (res) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, SignUpResponse(res.data ?: true, " user added successfully"))
            }

            is Resource.Error -> {
                call.respond(
                    HttpStatusCode.Conflict,
                    SignUpResponse(false, res.message ?: " something went wrong from server")
                )
            }
        }
    }

}


fun Route.signIn(
    hashingService: HashingService,
    userDataManager: UserDataManager,
    tokenService: TokenService,
    config: TokenConfig
) {

    post("signIn") {
        val req = call.receive<AuthenticationReq>()

        if (req.equals(null)) {
            call.respond(HttpStatusCode.BadRequest, SignInResponse(res = false, message = "BadRequest"))
            return@post
        }

        val user = userDataManager.getUserByEmail(req.email)
        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, SignInResponse(false, message = "Incorrect username or password"))
            return@post
        }

        val isPassValid = hashingService.verify(
            userPass = req.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isPassValid) {
            call.respond(HttpStatusCode.Unauthorized, SignInResponse(false, message = "Incorrect username or password"))
            return@post
        }

        val token = tokenService.generate(
            config = config,
            claims = arrayOf(
                TokenClaim(
                    name = "username",
                    value = user.username
                ),
                TokenClaim(
                    name = "email",
                    value = user.email
                )
            )
        )
        call.respond(HttpStatusCode.OK, SignInResponse(true, token))
    }

}


fun Route.authenticateUser() {
    //when user logged in and then relaunched the app
    //here we check if user's token is still valid or not
    //if valid -> user authenticated (HttpStatusCode.Accepted)
    //if invalid -> login again (unauthorized status code -> HttpStatusCode.Unauthorized )

    //if you wanna get token from header just use authenticate fun like below:

    authenticate {
        //add token in header as Authorization to make sure user is authorized
        get("authenticate") {
            call.respond(HttpStatusCode.Accepted)
        }
    }
}


fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userID", String::class).toString()
            val username = principal?.getClaim("username", String::class).toString()
            val email = principal?.getClaim("email", String::class).toString()
            call.respond(
                HttpStatusCode.OK, SecretInfo(
                    userId = userId,
                    username = username,
                    email = email
                )
            )
        }
    }

}


fun Route.updateUsername(
    userDataManager: UserDataManager
) {
    authenticate {
        post<UpdateUsername>("updateUsername") {
            if (it.email.isNotBlank() || it.username.isNotBlank()) {
                userDataManager.updateUsername(it.email, it.username)
                call.respond(HttpStatusCode.Accepted, "username changed")
            } else {
                call.respond(HttpStatusCode.BadRequest, "null email or username ")
            }
        }
    }

}


fun Route.updatePass(
    userDataManager: UserDataManager ,
    hashingService: HashingService
) {
        post<UpdatePassword>("/updatePass") {
            if (it.email.isBlank() && it.newPassword.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, SignUpResponse(false, "invalid information"))
            }
            val user = userDataManager.getUserByEmail(it.email)
            if (user!=null){
                val isPassValid = hashingService.verify(
                    userPass = it.oldPass,
                    saltedHash = SaltedHash(
                        hash = user.password,
                        salt = user.salt
                    )
                )
                if (isPassValid){
                    val res = userDataManager.updatePassword(it.email, it.newPassword)
                    if (res) {
                        call.respond(HttpStatusCode.OK, SignUpResponse(true, "password updated"))
                    } else {
                        call.respond(HttpStatusCode.ExpectationFailed, SignUpResponse(false, "something went wrong in server"))
                    }

                }
            }else{
                call.respond(HttpStatusCode.OK, SignUpResponse(true, "wrong email or password"))
            }
        }
}



























