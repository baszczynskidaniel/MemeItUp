package org.example.project.di

import io.ktor.client.engine.HttpClientEngine
import okhttp3.OkHttpClient
import org.example.project.settings.data.DataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory(androidApplication()) }
        single<HttpClientEngine> {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            val sslSocketFactory = sslContext.socketFactory


            val client = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }

                .build()

            io.ktor.client.engine.cio.CIO.create {
                https {
                    trustManager = trustAllCerts[0] as X509TrustManager
                }
            }
        }
    }