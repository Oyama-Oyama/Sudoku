package net

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import models.DIFFICULTY_EASY
import models.DIFFICULTY_EXPERT
import models.DIFFICULTY_HARD
import models.DIFFICULTY_MEDIUM
import models.GameItem


//{
//    "code": 0,
//    "msg": "Success",
//    "data": {
//    "mission": "063004970708065002000890053004100890321700000007456000240030000000642305130000406",
//    "solution": "563214978798365142412897653654123897321789564987456231246531789879642315135978426"
//},
//    "timestamp": 1713859271801
//}

@Serializable
data class GameItemResponse(val code: Int, val msg: String, val data: GameItem, val timestamp: Long)

val httpClient = HttpClient(CIO) {
    expectSuccess = true
    engine {
        maxConnectionsCount = 1000
        endpoint {
            connectTimeout = 2 * 1000
//            requestTimeout = 2 * 1000
            connectAttempts = 2
        }
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
    install(HttpRequestRetry) {
        maxRetries = 2
        exponentialDelay()
    }
}


//difficulty_level
//string
//可选
//难易程度，可选值：easy|medium|hard|extreme
suspend fun requestGameItem(type: Int): Result<GameItem> {
    try {
        val type = when (type) {
            DIFFICULTY_EASY -> "easy"
            DIFFICULTY_MEDIUM -> "medium"
            DIFFICULTY_HARD -> "hard"
            DIFFICULTY_EXPERT -> "extreme"
            else -> "easy"
        }
        val path = "http://101.37.30.6:8888/api/generator"
        val response: HttpResponse = httpClient.get(path) {
            method = HttpMethod.Get
            url {
                parameters.append("difficulty_level", type)
            }
        }
        val result: String = response.body<String>()
        val item = Json.decodeFromString<GameItemResponse>(result)
        return Result.success(item.data)
    } catch (e: Exception) {
        println("${e.cause}")
        println("${e.message}")
        return Result.failure(e)
    }
}