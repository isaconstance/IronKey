package br.com.isabella.ironkey

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import br.com.isabella.ironkey.ui.theme.IronkeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IronkeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IronKeyForm(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IronKeyForm(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var generatedPassword by remember { mutableStateOf("") }
    var maxCharacters by remember { mutableStateOf(8) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ironman),
            contentDescription = "Logo App",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop

        )
        Text(
            text = "Senhas seguras",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ){
            Column {
                OutlinedTextField(
                    value = generatedPassword,
                    onValueChange = { newPassword ->
                        if (newPassword.length <= maxCharacters)
                            generatedPassword = newPassword
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password"
                        )
                    },
                    trailingIcon = {
                        if (generatedPassword.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copiar",
                                modifier = Modifier.clickable {
                                    copyPassword(context, generatedPassword)
                                }
                            )

                        }
                    }
                )

                Text("${generatedPassword.length} / $maxCharacters",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                        .padding(end = 8.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IronManFormPreview(modifier: Modifier = Modifier) {
    IronkeyTheme {
        IronKeyForm()
    }
}

fun copyPassword(context: Context, password: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
    val clip = ClipData.newPlainText("Senha", password)
    clipboardManager.setPrimaryClip(clip)
    Toast.makeText(context, "Senha copiada!",
        Toast.LENGTH_SHORT).show()
}