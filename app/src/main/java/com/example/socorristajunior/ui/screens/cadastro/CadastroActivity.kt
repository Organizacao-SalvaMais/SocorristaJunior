package com.example.socorristajunior.ui.screens.cadastro
/*
class CadastroActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nome = findViewById<EditText>(R.id.edtNomeCompleto)
        val email = findViewById<EditText>(R.id.edtEmail)
        val telefone = findViewById<EditText>(R.id.edtTelefone)
        val genero = findViewById<Spinner>(R.id.spinnerGenero)
        val nascimento = findViewById<EditText>(R.id.edtDataNascimento)
        val senha = findViewById<EditText>(R.id.edtSenha)
        val confirmarSenha = findViewById<EditText>(R.id.edtConfirmarSenha)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Selecione", "Feminino", "Masculino", "Outro"))
        genero.adapter = adapter

        // Máscara de telefone
        telefone.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "(##) #####-####"
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val digits = s.toString().replace(Regex("\\D"), "")
                var formatted = ""
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m == '#') {
                        if (i >= digits.length) break
                        formatted += digits[i]
                        i++
                    } else {
                        formatted += m
                    }
                }
                telefone.setText(formatted)
                telefone.setSelection(formatted.length)
                isUpdating = false
            }
        })

        btnCadastrar.setOnClickListener {
            viewModel.register(
                nome.text.toString(),
                email.text.toString(),
                telefone.text.toString(),
                genero.selectedItem.toString(),
                nascimento.text.toString(),
                senha.text.toString(),
                confirmarSenha.text.toString()
            )
        }

        //viewModel.loading.observe(this) { progressBar.isVisible = it }//progressBar ta dando erro, tem algum import que resolva?

        viewModel.registerStatus.observe(this) { mensagem ->
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
        }
    }
}
 */