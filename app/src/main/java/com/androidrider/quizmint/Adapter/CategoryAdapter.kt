package com.androidrider.quizmint.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.quizmint.Model.SubjectModel
import com.androidrider.quizmint.Activity.PlayScreen
import com.androidrider.quizmint.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter(private val context: Context, private var subjectList: List<SubjectModel>) :
    RecyclerView.Adapter<CategoryAdapter.SubjectViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjectList[position])
    }

    override fun getItemCount() = subjectList.size

    inner class SubjectViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: SubjectModel) {
            binding.categoryName.text = subject.subject
            Glide.with(context).load(subject.img).into(binding.subjectImage)

            binding.root.setOnClickListener {
                val intent = Intent(context, PlayScreen::class.java)
                intent.putExtra("subjectName", subject.subject) // Pass the subject name
                intent.putExtra("subjectImage", subject.img) // Pass the subject name
                context.startActivity(intent)
            }
        }
    }


    fun updateList(newList: List<SubjectModel>) {
        subjectList = newList
        notifyDataSetChanged()
    }

}
