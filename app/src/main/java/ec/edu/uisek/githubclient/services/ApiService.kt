package ec.edu.uisek.githubclient.services
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.models.RepositoryPayload
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("user/repos")
    suspend fun getRepository (
        @Query(value = "sort") sort: String = "Created",
        @Query(value = "direction") direction: String = "desc",
        @Query(value = "affiliation") affiliation: String = "owner",
        @Query(value = "t") t: String = "${System.currentTimeMillis()}",
    ): List<Repository>

    @GET("user")
    suspend fun getUser(): ec.edu.uisek.githubclient.models.GithubUser

    @POST ("user/repos")
    suspend fun createRepository(
        @Body payload: RepositoryPayload
    ): Repository

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): retrofit2.Response<Unit?>

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body payload: RepositoryPayload
    ): Repository
}
