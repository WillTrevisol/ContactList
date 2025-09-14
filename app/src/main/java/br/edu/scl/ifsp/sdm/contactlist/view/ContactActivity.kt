package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {

    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(acb.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)

        val receivedContact = intent.getParcelableExtra<Contact?>(EXTRA_CONTACT)
        receivedContact?.let {
            val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
            with(acb) {
                if (viewContact) {
                    nameEditText.isEnabled = false
                    phoneEditText.isEnabled = false
                    emailEditText.isEnabled = false
                    addressEditText.isEnabled = false
                    saveButton.visibility = GONE
                }

                nameEditText.setText(it.name)
                phoneEditText.setText(it.phone)
                emailEditText.setText(it.email)
                addressEditText.setText(it.address)
            }
        }

        with(acb) {
            saveButton.setOnClickListener {
                val contact = Contact(
                    id = receivedContact?.id ?: hashCode(),
                    name = nameEditText.text.toString(),
                    phone = phoneEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    address = addressEditText.text.toString(),
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }


    }
}
