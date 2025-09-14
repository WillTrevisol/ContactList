package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact
import br.edu.scl.ifsp.sdm.contactlist.view.MainActivity
import br.edu.scl.ifsp.sdm.contactlist.view.OnContactClickListener

class ContactRecyclerViewAdapter(
    private val contactList: MutableList<Contact>,
    private val onContactClickListener: OnContactClickListener
): RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(tileContactBinding: TileContactBinding): ViewHolder(tileContactBinding.root) {
        val nameTextView: TextView = tileContactBinding.nameTextView
        val emailTextView: TextView = tileContactBinding.emailTextView

        init {
            tileContactBinding.root.apply {
                setOnCreateContextMenuListener { menu, _, _ ->
                    (onContactClickListener as AppCompatActivity).menuInflater.inflate(
                        R.menu.context_menu_main,
                        menu
                    )

                    menu.findItem(R.id.removeContactMenuItem).setOnMenuItemClickListener {
                        onContactClickListener.onRemoveContactMenuItemClick(adapterPosition)
                        true
                    }

                    menu.findItem(R.id.editContactMenuItem).setOnMenuItemClickListener {
                        onContactClickListener.onEditContactMenuItemClick(adapterPosition)
                        true
                    }
                }
                setOnClickListener {
                    onContactClickListener.onContactClick(adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = contactList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
         TileContactBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ContactViewHolder(this)
        }


    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) {
        contactList[position].also {
            with(holder) {
                nameTextView.text = it.name
                emailTextView.text = it.email
            }
        }
    }
}
