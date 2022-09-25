package com.example.domain.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.api.ApiService
import com.example.domain.di.domainModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object MockAPI {
    val server = MockWebServer()

    private val successResponse = "[{\"id\":1,\"name\":\"Serjio Marquina\",\"alias\":\"Professor\",\"occupation\":\"Heist Planner\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Lisbon\",\"family\":\"Berlin\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Alvaro Morte\",\"image\":\"https://wallpapercave.com/wp/wp5988791.jpg\"},{\"id\":2,\"name\":\"Andres de Fonollosa\",\"alias\":\"Berlin\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"Tatiana\",\"family\":\"Professor\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Pedro Alonsa\",\"image\":\"https://wallpapercave.com/wp/wp5475153.jpg\"},{\"id\":3,\"name\":\"Silene Oliveira\",\"alias\":\"Tokyo\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Rio\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Ursula Corbera\",\"image\":\"https://wallpapercave.com/wp/wp8638125.jpg\"},{\"id\":4,\"name\":\"Agata Jimenez\",\"alias\":\"Nairobi\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Deceased\",\"romance\":\"Bogota\",\"family\":\"Axel\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E7\",\"played_by\":\"Alba Flores\",\"image\":\"https://wallpapercave.com/wp/wp5162352.jpg\"},{\"id\":5,\"name\":\"Raquel Murillo Fuentes\",\"alias\":\"Lisbon\",\"occupation\":\"Police, Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Professor\",\"family\":\"Marivi Fuentess, Paula Murillo\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Itziar Ituno\",\"image\":\"https://upload.wikimedia.org/wikipedia/en/9/9f/Raquel_Murillo.jpg\"},{\"id\":6,\"name\":\"Daniel Ramos\",\"alias\":\"Denver\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Stockholm\",\"family\":\"Moscow, Cincinnati, Manila\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Jaime Lorente\",\"image\":\"https://wallpapercave.com/wp/wp6081631.jpg\"},{\"id\":7,\"name\":\"Anibal Cortes\",\"alias\":\"Rio\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Tokyo\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Miguel Herran\",\"image\":\"https://clusterchannel.com/wp-content/uploads/2021/05/Rio.jpg\"},{\"id\":8,\"name\":\"Agustin Ramos\",\"alias\":\"Moscow\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"\",\"family\":\"Denver\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E8\",\"played_by\":\"Paco Tous\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/f/f6/Moscow_-_Part_1_Promo.jpg/revision/latest?cb=20190918223156\"},{\"id\":9,\"name\":\"Mirko Dragic\",\"alias\":\"Helsinki\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Palermo\",\"family\":\"Oslo\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Darko Peric\",\"image\":\"https://www.pixel4k.com/wp-content/uploads/2020/01/helsinki-in-money-heist_1578252282.jpg\"},{\"id\":10,\"name\":\"Radko Dargic\",\"alias\":\"Oslo\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"\",\"family\":\"Helsinki\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E1\",\"played_by\":\"Roberto Garcia\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/f/f4/Oslo.jpeg/revision/latest?cb=20200316105225\"},{\"id\":11,\"name\":\"Monica Gaztambide\",\"alias\":\"Stockholm\",\"occupation\":\"Secretary, Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Denver\",\"family\":\"Cincinnati\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Esther Acebo\",\"image\":\"https://wallpapercave.com/wp/wp6578056.jpg\"},{\"id\":12,\"name\":\"Martin Berrote\",\"alias\":\"Palermo\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Berlin, Helsinki\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Rodrigo de la Serna\",\"image\":\"https://wallpaperaccess.com/full/2703615.jpg\"},{\"id\":13,\"name\":\"\",\"alias\":\"Bogota\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Nairobi\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Hovik Keuchkerian\",\"image\":\"https://wallpaperaccess.com/full/4999191.png\"},{\"id\":14,\"name\":\"\",\"alias\":\"Marseille\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Luka Peros\",\"image\":\"https://pbs.twimg.com/media/EUxAM1bU0AAa8hi.jpg\"},{\"id\":15,\"name\":\"\",\"alias\":\"Matias Cano\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E3\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Ahikar Azcona\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/6/69/Mat%C3%ADas_Ca%C3%B1o.jpg/revision/latest?cb=20200511195143\"},{\"id\":16,\"name\":\"Julia Ramos\",\"alias\":\"Manila\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"Denver, Moscow\",\"first_appearance\":\"P3 E7\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Belen Cuesta\",\"image\":\"https://images.summitmedia-digital.com/preview/images/2020/04/04/manila-nm.jpg\"},{\"id\":17,\"name\":\"Angel Rubio\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Mari Carmen\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Fernando Soto\",\"image\":\"https://freaky5.com/wp-content/uploads/2020/03/angel-freaky5.jpg\"},{\"id\":18,\"name\":\"Alfonso Prieto\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Marcia\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E7\",\"played_by\":\"Juan Fernandez\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/3/30/Prieto.jpg/revision/latest?cb=20200411013303\"},{\"id\":19,\"name\":\"Alicia Seirra\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"German\",\"family\":\"\",\"first_appearance\":\"P3 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Najwa Nimri\",\"image\":\"https://tv-fanatic-res.cloudinary.com/iu/s--jneObtVc--/f_auto,q_auto/v1595553437/who-is-alicia-sierra\"},{\"id\":20,\"name\":\"Luis Tamayo\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Fernando Sayo\",\"image\":\"https://piks.eldesmarque.com/bin/2020/04/07/fernando_cayo_tamayo_001.jpg\"},{\"id\":21,\"name\":\"Suarez\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Mario de la Rosa\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/5/51/Su%C3%A1rez.jpg/revision/latest?cb=20210114034602\"},{\"id\":22,\"name\":\"Alberto Vicuna\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Raquel Murillo, Laura Murillo\",\"family\":\"\",\"first_appearance\":\"P1 E4\",\"last_appearance\":\"P4 E3\",\"played_by\":\"Miquel Garcia Borda\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/b/ba/Alberto_Vicu%C3%B1a.jpg/revision/latest?cb=20210113063532\"},{\"id\":23,\"name\":\"Arturo Roman\",\"alias\":\"\",\"occupation\":\"Director of Royal Mint of Spain\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Monica Gaztambide\",\"family\":\"Laura\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Enrique Arce\",\"image\":\"https://filmdaily.co/wp-content/uploads/2020/04/Money-Heist-Arturo-Roman-lede.jpg\"},{\"id\":24,\"name\":\"Alison Parker\",\"alias\":\"\",\"occupation\":\"Student\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Pablo Ruiz\",\"family\":\"Benjamin Parker\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Maria Pedraza\",\"image\":\"https://images.wallpapersden.com/image/download/alison-parker-in-money-heist_a2xnamWUmZqaraWkpJRnbmhnrWduaGc.jpg\"},{\"id\":25,\"name\":\"Ariadna Cascales\",\"alias\":\"\",\"occupation\":\"Royal Mint Employee\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Berlin\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Clara Alvarado\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/8/83/Ariadna.jpg/revision/latest?cb=20200511024944\"},{\"id\":26,\"name\":\"Mercedes Colmenar\",\"alias\":\"\",\"occupation\":\"Teacher\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Anna Gras\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/8/83/Mercedes_Colmenar.jpg/revision/latest?cb=20210114034807\"},{\"id\":27,\"name\":\"Francisco Torres\",\"alias\":\"\",\"occupation\":\"Royal Mint Employee\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Antonio Cuellar Rodriguez\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/c/c3/Francisco_Torres.jpg/revision/latest/scale-to-width-down/450?cb=20210128164423\"},{\"id\":28,\"name\":\"Caesar Gandia\",\"alias\":\"\",\"occupation\":\"Security Agent\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E3\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Jose Manuel Poga\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/0/0b/Gandia.png/revision/latest?cb=20200506054726\"}]"
    private val errorResponse = "{ statusCode: 400, statusMessage: \"Testing\" }"

    fun enqueueSuccessResponse() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(successResponse)
        )
    }

    fun enqueueClientFailure() {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(errorResponse)
        )
    }

    fun enqueueServerFailure() {
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
        )
    }
}

@ExperimentalCoroutinesApi
class UserRemoteDataSourceTest: KoinTest {

//    @get:Rule
//    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()


    private val mockServer = MockWebServer()

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(mockServer.url("/"))
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    lateinit var apiService: ApiService

    private lateinit var userRemoteDataSource: UsersRemoteDataSource

    @Before
    fun setUp() {
//        startKoin {
//            domainModule
//        }
        userRemoteDataSource = UsersRemoteDataSource(retrofit.create(ApiService::class.java), retrofit)
    }

    @After
    fun after() {
//        stopKoin()
    }

    @Test
    fun `given users when fetchUsers returns Success`() = runBlocking {
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(successResponse)
        )

        val fetchedCharacters = userRemoteDataSource.fetchUsers()
        assert(fetchedCharacters.data?.size == 28)
    }

    @Test
    fun `given users When fetchUsers returns Error`() = runBlocking {
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(errorResponse)
        )

        val fetchedCharacters = userRemoteDataSource.fetchUsers()
        assert(fetchedCharacters.message == "Error fetching Characters")
    }

    @Test
    fun `given users When fetchUsers returns Server Error`() = runBlocking {
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(500)
        )
        val fetchedCharacters = userRemoteDataSource.fetchUsers()
        assert(fetchedCharacters.message == "Error fetching Characters")
    }
}

val successResponse = "[{\"id\":1,\"name\":\"Serjio Marquina\",\"alias\":\"Professor\",\"occupation\":\"Heist Planner\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Lisbon\",\"family\":\"Berlin\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Alvaro Morte\",\"image\":\"https://wallpapercave.com/wp/wp5988791.jpg\"},{\"id\":2,\"name\":\"Andres de Fonollosa\",\"alias\":\"Berlin\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"Tatiana\",\"family\":\"Professor\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Pedro Alonsa\",\"image\":\"https://wallpapercave.com/wp/wp5475153.jpg\"},{\"id\":3,\"name\":\"Silene Oliveira\",\"alias\":\"Tokyo\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Rio\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Ursula Corbera\",\"image\":\"https://wallpapercave.com/wp/wp8638125.jpg\"},{\"id\":4,\"name\":\"Agata Jimenez\",\"alias\":\"Nairobi\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Deceased\",\"romance\":\"Bogota\",\"family\":\"Axel\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E7\",\"played_by\":\"Alba Flores\",\"image\":\"https://wallpapercave.com/wp/wp5162352.jpg\"},{\"id\":5,\"name\":\"Raquel Murillo Fuentes\",\"alias\":\"Lisbon\",\"occupation\":\"Police, Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Professor\",\"family\":\"Marivi Fuentess, Paula Murillo\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Itziar Ituno\",\"image\":\"https://upload.wikimedia.org/wikipedia/en/9/9f/Raquel_Murillo.jpg\"},{\"id\":6,\"name\":\"Daniel Ramos\",\"alias\":\"Denver\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Stockholm\",\"family\":\"Moscow, Cincinnati, Manila\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Jaime Lorente\",\"image\":\"https://wallpapercave.com/wp/wp6081631.jpg\"},{\"id\":7,\"name\":\"Anibal Cortes\",\"alias\":\"Rio\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Tokyo\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Miguel Herran\",\"image\":\"https://clusterchannel.com/wp-content/uploads/2021/05/Rio.jpg\"},{\"id\":8,\"name\":\"Agustin Ramos\",\"alias\":\"Moscow\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"\",\"family\":\"Denver\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E8\",\"played_by\":\"Paco Tous\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/f/f6/Moscow_-_Part_1_Promo.jpg/revision/latest?cb=20190918223156\"},{\"id\":9,\"name\":\"Mirko Dragic\",\"alias\":\"Helsinki\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Palermo\",\"family\":\"Oslo\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Darko Peric\",\"image\":\"https://www.pixel4k.com/wp-content/uploads/2020/01/helsinki-in-money-heist_1578252282.jpg\"},{\"id\":10,\"name\":\"Radko Dargic\",\"alias\":\"Oslo\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Deceased\",\"romance\":\"\",\"family\":\"Helsinki\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E1\",\"played_by\":\"Roberto Garcia\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/f/f4/Oslo.jpeg/revision/latest?cb=20200316105225\"},{\"id\":11,\"name\":\"Monica Gaztambide\",\"alias\":\"Stockholm\",\"occupation\":\"Secretary, Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Denver\",\"family\":\"Cincinnati\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Esther Acebo\",\"image\":\"https://wallpapercave.com/wp/wp6578056.jpg\"},{\"id\":12,\"name\":\"Martin Berrote\",\"alias\":\"Palermo\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Berlin, Helsinki\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Rodrigo de la Serna\",\"image\":\"https://wallpaperaccess.com/full/2703615.jpg\"},{\"id\":13,\"name\":\"\",\"alias\":\"Bogota\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Nairobi\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Hovik Keuchkerian\",\"image\":\"https://wallpaperaccess.com/full/4999191.png\"},{\"id\":14,\"name\":\"\",\"alias\":\"Marseille\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Luka Peros\",\"image\":\"https://pbs.twimg.com/media/EUxAM1bU0AAa8hi.jpg\"},{\"id\":15,\"name\":\"\",\"alias\":\"Matias Cano\",\"occupation\":\"Robber\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E3\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Ahikar Azcona\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/6/69/Mat%C3%ADas_Ca%C3%B1o.jpg/revision/latest?cb=20200511195143\"},{\"id\":16,\"name\":\"Julia Ramos\",\"alias\":\"Manila\",\"occupation\":\"Robber\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"Denver, Moscow\",\"first_appearance\":\"P3 E7\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Belen Cuesta\",\"image\":\"https://images.summitmedia-digital.com/preview/images/2020/04/04/manila-nm.jpg\"},{\"id\":17,\"name\":\"Angel Rubio\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Mari Carmen\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Fernando Soto\",\"image\":\"https://freaky5.com/wp-content/uploads/2020/03/angel-freaky5.jpg\"},{\"id\":18,\"name\":\"Alfonso Prieto\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Marcia\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E7\",\"played_by\":\"Juan Fernandez\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/3/30/Prieto.jpg/revision/latest?cb=20200411013303\"},{\"id\":19,\"name\":\"Alicia Seirra\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"German\",\"family\":\"\",\"first_appearance\":\"P3 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Najwa Nimri\",\"image\":\"https://tv-fanatic-res.cloudinary.com/iu/s--jneObtVc--/f_auto,q_auto/v1595553437/who-is-alicia-sierra\"},{\"id\":20,\"name\":\"Luis Tamayo\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Fernando Sayo\",\"image\":\"https://piks.eldesmarque.com/bin/2020/04/07/fernando_cayo_tamayo_001.jpg\"},{\"id\":21,\"name\":\"Suarez\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E2\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Mario de la Rosa\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/5/51/Su%C3%A1rez.jpg/revision/latest?cb=20210114034602\"},{\"id\":22,\"name\":\"Alberto Vicuna\",\"alias\":\"\",\"occupation\":\"Police\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Raquel Murillo, Laura Murillo\",\"family\":\"\",\"first_appearance\":\"P1 E4\",\"last_appearance\":\"P4 E3\",\"played_by\":\"Miquel Garcia Borda\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/b/ba/Alberto_Vicu%C3%B1a.jpg/revision/latest?cb=20210113063532\"},{\"id\":23,\"name\":\"Arturo Roman\",\"alias\":\"\",\"occupation\":\"Director of Royal Mint of Spain\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"Monica Gaztambide\",\"family\":\"Laura\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Enrique Arce\",\"image\":\"https://filmdaily.co/wp-content/uploads/2020/04/Money-Heist-Arturo-Roman-lede.jpg\"},{\"id\":24,\"name\":\"Alison Parker\",\"alias\":\"\",\"occupation\":\"Student\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Pablo Ruiz\",\"family\":\"Benjamin Parker\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Maria Pedraza\",\"image\":\"https://images.wallpapersden.com/image/download/alison-parker-in-money-heist_a2xnamWUmZqaraWkpJRnbmhnrWduaGc.jpg\"},{\"id\":25,\"name\":\"Ariadna Cascales\",\"alias\":\"\",\"occupation\":\"Royal Mint Employee\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"Berlin\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Clara Alvarado\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/8/83/Ariadna.jpg/revision/latest?cb=20200511024944\"},{\"id\":26,\"name\":\"Mercedes Colmenar\",\"alias\":\"\",\"occupation\":\"Teacher\",\"gender\":\"Female\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Anna Gras\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/8/83/Mercedes_Colmenar.jpg/revision/latest?cb=20210114034807\"},{\"id\":27,\"name\":\"Francisco Torres\",\"alias\":\"\",\"occupation\":\"Royal Mint Employee\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P1 E1\",\"last_appearance\":\"P2 E9\",\"played_by\":\"Antonio Cuellar Rodriguez\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/c/c3/Francisco_Torres.jpg/revision/latest/scale-to-width-down/450?cb=20210128164423\"},{\"id\":28,\"name\":\"Caesar Gandia\",\"alias\":\"\",\"occupation\":\"Security Agent\",\"gender\":\"Male\",\"status\":\"Alive\",\"romance\":\"\",\"family\":\"\",\"first_appearance\":\"P3 E3\",\"last_appearance\":\"P4 E8\",\"played_by\":\"Jose Manuel Poga\",\"image\":\"https://static.wikia.nocookie.net/money-heist/images/0/0b/Gandia.png/revision/latest?cb=20200506054726\"}]"
val errorResponse = "{ statusCode: 400, statusMessage: \"Testing\" }"