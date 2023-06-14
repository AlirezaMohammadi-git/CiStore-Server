package com.pixel_Alireza.routing.auth

import com.pixel_Alireza.data.request.AuthenticationReq
import com.pixel_Alireza.data.response.auth.SecretInfo
import com.pixel_Alireza.data.response.auth.SignInResponse
import com.pixel_Alireza.data.response.auth.SignUpResponse
import com.pixel_Alireza.data.user.User
import com.pixel_Alireza.data.user.UserDataManager
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

        if (req.username == null){
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Fields blank or short pass exception"))
            return@post
        }else{
            username = req.username
        }

        //<editor-fold desc="isFormatCorrect?">
        val isFieldsBlank = req.password.isBlank() || req.username.isBlank()
        val isPasswordTooShort = req.password.length < 8
        if (isFieldsBlank || isPasswordTooShort) {
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Fields blank or short pass exception"))
            return@post
        }
        //</editor-fold>

        val sameUsername = userDataManager.sameUsernameChecker(username = username)
        val sameEmail = userDataManager.sameEmailChecker(req.email)

        if (sameEmail || sameUsername) {
            call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Username or Email already exist!"))
        } else {
            val saltedHash = hashingService.generateSaltedHash(req.password)
            val user = User(
                username = req.username,
                email = req.email,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
            val res = userDataManager.insertNewUser(user)
            if (res) {
                call.respond(HttpStatusCode.OK, SignUpResponse(res, "user added successfully"))
            } else {
                call.respond(HttpStatusCode.Conflict, SignUpResponse(false, "Something went wrong (Server)"))
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
                    name = "userID",
                    value = user.id.toString()
                ),
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