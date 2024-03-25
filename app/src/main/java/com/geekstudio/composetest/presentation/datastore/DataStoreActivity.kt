package com.geekstudio.composetest.presentation.datastore

import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.geekstudio.composetest.presentation.base.BaseActivity
import com.geekstudio.composetest.ui.theme.ComposeTestTheme
import com.geekstudio.composetest.ui.view.TwoButtonCard
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.Signature
import java.security.UnrecoverableEntryException
import java.security.cert.CertificateException
import java.util.Enumeration
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


@AndroidEntryPoint
class DataStoreActivity : BaseActivity() {
    private val viewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAndStoreSecretKey()
        val encrypt = encryptWithKeyStore("text")
        Log.d("DataStoreActivity", "KeyStore encrypt = ${encrypt?.decodeToString()}")

        val decrypt = decryptWithKeyStore(encrypt ?: byteArrayOf())
        Log.d("DataStoreActivity", "KeyStore decrypt = $decrypt")

        initView()
        initUiObserver()
    }

    private val ANDROID_KEY_STORE_PROVIDER = "AndroidKeyStore"
    private val ANDROID_KEY_STORE_ALIAS = "AES_KEY_DEMO"

    @Throws(
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class
    )
    private fun createAndStoreSecretKey() {
        val builder: KeyGenParameterSpec.Builder = KeyGenParameterSpec.Builder(
            ANDROID_KEY_STORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        val keySpec: KeyGenParameterSpec = builder
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(false)
            .build()
        val aesKeyGenerator: KeyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE_PROVIDER)
        aesKeyGenerator.init(keySpec)
        val key: SecretKey = aesKeyGenerator.generateKey()
    }

    @Throws(
        KeyStoreException::class,
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class,
        IOException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun geKeyStoreSecretKey(): SecretKey? {
        // Initialize KeyStore
        val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER)
        keyStore.load(null)
        // Retrieve the key with alias androidKeyStoreAlias created before
        val keyEntry: KeyStore.SecretKeyEntry? =
            keyStore.getEntry(ANDROID_KEY_STORE_ALIAS, null) as? KeyStore.SecretKeyEntry

        return keyEntry?.secretKey
    }

    private val secureRandom = SecureRandom()
    private val gcmIvLength = 12

    private fun encryptWithKeyStore(plainText: String): ByteArray? {
        val key = geKeyStoreSecretKey() ?: return null

        //보안 값 랜덤으로 추가
        val iv = ByteArray(gcmIvLength)
        secureRandom.nextBytes(iv)

        //
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val parameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec)
        val encryptText = cipher.doFinal(plainText.toByteArray())

        val byteBuffer = ByteBuffer.allocate(iv.size + encryptText.size)
        byteBuffer.put(iv)
        byteBuffer.put(encryptText)
        return byteBuffer.array()
    }

    private fun decryptWithKeyStore(encryptText: ByteArray): String? {
        val key = geKeyStoreSecretKey() ?: return null
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, encryptText, 0, gcmIvLength))
        val plainText = cipher.doFinal(encryptText, gcmIvLength, encryptText.size - gcmIvLength)
        return String(plainText, StandardCharsets.UTF_8)
    }


    private fun executeKeyStore() {
        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
        val aliases: Enumeration<String> = ks.aliases()

        for (alias in aliases) {
            Log.d("DataStoreActivity", "KeyStore alias = $alias")
            val entry: KeyStore.Entry = ks.getEntry(alias, null)
            if (entry !is KeyStore.PrivateKeyEntry) {
                Log.w("DataStoreActivity", "KeyStore Not an instance of a PrivateKeyEntry")
                continue
            }

            Log.d(
                "DataStoreActivity",
                "KeyStore privateKey encoded = ${entry.privateKey.encoded}, format = ${entry.privateKey.format}, algorithm = ${entry.privateKey.algorithm}"
            )
            val signature: ByteArray = Signature.getInstance("SHA512withRSA").run {
                initSign(entry.privateKey)
                update("테스트테스트테스트".toByteArray())
                sign()
            }

            Log.d("DataStoreActivity", "KeyStore signature = ${signature.contentToString()}")
        }
    }

    private fun initUiObserver() {
        repeatOnStarted {
            /*viewModel.isDarkThemeFlow.collect {
                Log.d("DataStoreActivity", "initUiObserver SettingData = $it")
                viewModel.currentSettingData = it
            }*/

            viewModel.testDataFlow.collect {
                Log.d("DataStoreActivity", "initUiObserver isTest = ${it.isTest}")
                viewModel.currentTestData = it
            }
        }
    }

    private fun initView() {
        setContent {
            ComposeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TwoButtonCard({
                        if (viewModel.currentSettingData != null) {
                            val isDarkTheme = viewModel.currentSettingData?.isDarkTheme ?: false
                            viewModel.updateDarkTheme(!isDarkTheme)
                        }
                    }, {
                        Log.d(
                            "DataStoreActivity",
                            "test button currentTestData = ${viewModel.currentTestData?.isTest}"
                        )
                        if (viewModel.currentTestData != null) {
                            val isTest = viewModel.currentTestData?.isTest ?: false
                            viewModel.updateTest(!isTest)
                        }
                    })
                }
            }
        }
    }
}

