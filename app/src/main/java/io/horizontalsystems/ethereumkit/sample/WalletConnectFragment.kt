package io.horizontalsystems.ethereumkit.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trustwallet.walletconnect.WCClient
import com.trustwallet.walletconnect.models.WCPeerMeta
import com.trustwallet.walletconnect.models.session.WCSession
import kotlinx.android.synthetic.main.fragment_wallet_connect.*
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WalletConnectFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(WalletConnectViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            viewModel.go(editText.text.toString())
        }
    }
}


class WalletConnectViewModel : ViewModel() {
    val wcClient = WCClient(httpClient = OkHttpClient.Builder().build())

    init {
        wcClient.addSocketListener(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.e("AAA", "Yahoo")
            }
        })

        val accounts = listOf("account1")
        val chainId = 234

        wcClient.onSessionRequest = { id: Long, peer: WCPeerMeta ->
            wcClient.approveSession(accounts, chainId)
        }
    }

    fun go(connectionString: String) {
        Log.e("AAA", connectionString.toString())

        WCSession.from(connectionString)?.let { wcSession ->
            wcClient.connect(wcSession, WCPeerMeta("name", "http://example.com"))
        }



    }

}