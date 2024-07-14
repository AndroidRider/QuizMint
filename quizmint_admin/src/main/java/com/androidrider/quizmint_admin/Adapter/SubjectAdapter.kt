package com.androidrider.quizmint_admin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.quizmint_admin.Activity.MainActivity
import com.androidrider.quizmint_admin.Activity.QuestionActivity
import com.androidrider.quizmint_admin.Activity.UserDetailActivity
import com.androidrider.quizmint_admin.Model.SubjectModel
import com.androidrider.quizmint_admin.R
import com.androidrider.quizmint_admin.databinding.ItemSubjectLayoutBinding
import com.bumptech.glide.Glide

class SubjectAdapter(
    val context: Context,
    val list: ArrayList<SubjectModel>) :
    RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_subject_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var data=list[position]

        holder.binding.tvSubjectName.text = list[position].subject

        Glide.with(context).load(list[position].img).into(holder.binding.imgSubjectImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuestionActivity::class.java).apply {
                putExtra("subjectName", data.subject)
            }
            context.startActivity(intent)
        }

    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding = ItemSubjectLayoutBinding.bind(view)
    }

}

