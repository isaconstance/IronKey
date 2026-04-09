package br.com.isabella.ironkey

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
    var passwordType by remember { mutableStateOf(PasswordType.PIN) }
    var isEditable by remember { mutableStateOf(false) }

    // Checkboxes
    var includeUppercase by remember {
        mutableStateOf(true) }
    var includeLowercase by remember {
        mutableStateOf(true) }
    var includeNumbers by remember {
        mutableStateOf(true) }
    var includeSymbols by remember {
        mutableStateOf(false) }

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
                    enabled = isEditable,
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

                Text("Tipo de senha")

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                            .clickable { passwordType = PasswordType.PIN }

                    ){
                        RadioButton(
                            selected = passwordType == PasswordType.PIN,
                            onClick = {passwordType = PasswordType.PIN}
                        )
                        Text("PIN")
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                            .clickable { passwordType = PasswordType.STANDARD}
                    ){
                        RadioButton(
                            selected = passwordType == PasswordType.STANDARD,
                            onClick = {passwordType = PasswordType.STANDARD}
                        )
                        Text("Padrão")
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
                    Icon(
                        imageVector = if (isEditable) Icons.Default.LockOpen else Icons.Filled.Lock,
                        contentDescription = ""
                    )
                    Text("Pertmitir editar senha?", modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isEditable, onCheckedChange = {isEditable = it})

                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(20.dp))

                if (isEditable){
                    Text("Tamanho da senha ${maxCharacters}")
                    Slider(
                        value = maxCharacters.toFloat(),
                        onValueChange = {maxCharacters = it.toInt()},
                        valueRange = 4.toFloat()..12.toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment
                    = Alignment.CenterVertically) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeUppercase,
                                onCheckedChange = { includeUppercase = it }
                            )
                            Text("Maiúsculas")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeLowercase,
                                onCheckedChange = { includeLowercase = it }
                            )
                            Text("Minúsculas")
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(checked = includeNumbers,
                                onCheckedChange = { includeNumbers = it }
                            )
                            Text("Números")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeSymbols,
                                onCheckedChange = { includeSymbols = it }
                            )
                            Text("Símbolos")
                        }
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        val generator : PasswordGenerator =
                            if (passwordType == PasswordType.PIN) PinPasswordGenerator()
                            else(
                                StandardPasswordGenerator(
                                    includeUppercase = includeUppercase,
                                    includeLowercase = includeLowercase,
                                    includeNumbers = includeNumbers,
                                    includeSymbols = includeSymbols
                                )

                            )

                        generatedPassword = generator.generate(maxCharacters)
                    }
                ) {
                    Text("Gerar senha")
                }
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

enum class PasswordType{
    PIN,
    STANDARD
}

interface PasswordGenerator{
    fun generate(length: Int): String
}

class PinPasswordGenerator : PasswordGenerator{
    override fun generate(length: Int): String {

        val digits = ('0'..'9')
        return (1..length)
            .map { digits.random() }
            .joinToString("")
    }
}

class StandardPasswordGenerator(
    private val includeUppercase: Boolean = true,
    private val includeLowercase: Boolean = true,
    private val includeNumbers: Boolean = true,
    private val includeSymbols: Boolean = true
) : PasswordGenerator {
    override fun generate(length: Int): String {
        val chars = buildList<Char> {
            if (includeUppercase) addAll('A'..'Z')
            if (includeLowercase) addAll('a'..'z')
            if (includeNumbers) addAll('0'..'9')
            if (includeSymbols)
                addAll("!@#\$%&*()_-+=<>?".toList())
        }
        if (chars.isEmpty()) return ""
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}