package com.falikiali.rschapp.data

import android.app.Application
import com.falikiali.rschapp.R
import com.falikiali.rschapp.data.remote.CategoryApiService
import com.falikiali.rschapp.data.remote.dto.res.BaseErrorResponse
import com.falikiali.rschapp.domain.model.Category
import com.falikiali.rschapp.domain.repository.CategoryRepository
import com.falikiali.rschapp.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryApiService: CategoryApiService, private val application: Application): CategoryRepository {
    override suspend fun getCategories(): Flow<ResultState<List<Category>>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val response = categoryApiService.getCategories()
                val mapResponse = response.data.map {
                    Category(
                        it.id,
                        it.name
                    )
                }

                val listCategory = mutableListOf<Category>()
                listCategory.add(0, Category(
                    0,
                    application.getString(R.string.all)
                ))
                listCategory.addAll(mapResponse)

                emit(ResultState.Success(listCategory))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.data != null) {
                        emit(ResultState.Failed(err.data.toString(), err.statusCode))
                    } else {
                        emit(ResultState.Failed(err.statusMessage, err.statusCode))
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }
}