package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRecyclerViewAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactRecyclerViewAdapter by lazy {
        ContactRecyclerViewAdapter(contactList, this)
    }

    private lateinit var contactActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        contactActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also { receivedContact ->
                    if (contactList.any { it.id == receivedContact.id }) {
                        val position = contactList.indexOfFirst{it.id == receivedContact.id}
                        contactList[position] = receivedContact
                        contactAdapter.notifyItemChanged(position)
                    } else {
                        contactList.add(receivedContact)
                        contactAdapter.notifyItemInserted(contactList.lastIndex)
                    }
                }
            }
        }

        fillContacts()

        amb.contactsListRecyclerView.adapter = contactAdapter
        amb.contactsListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMenuItem -> {
                contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java))
                true
            }

            else -> { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onContactClick(position: Int) {
        startActivity(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        })
    }

    override fun onEditContactMenuItemClick(position: Int) {
        contactActivityResultLauncher.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
        })
    }

    override fun onRemoveContactMenuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, getString(R.string.contact_removed), Toast.LENGTH_SHORT).show()
    }

    private fun fillContacts() {
        for (i in 1..10) {
            contactList.add(
                Contact(
                    id = i,
                    name = "Nome $i",
                    address = "Endereço $i",
                    phone = "Telefone $i",
                    email = "Email $i"
                )
            )
        }
    }
}
