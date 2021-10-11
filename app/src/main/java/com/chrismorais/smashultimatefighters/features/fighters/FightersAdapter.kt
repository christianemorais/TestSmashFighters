package com.chrismorais.smashultimatefighters.features.fighters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chrismorais.smashultimatefighters.R
import com.chrismorais.smashultimatefighters.data.repository.model.Fighter

class FightersAdapter(private val list: MutableList<Fighter>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<FightersAdapter.ViewHolder>() {

    fun updateList(fighters: List<Fighter>) {
        list.clear()
        list.addAll(fighters)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_fighter, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivFighterThumb: ImageView = view.findViewById(R.id.iv_fighter_thumb)
        private val tvFighterName: TextView = view.findViewById(R.id.tv_fighter_name)
        private val tvFighterUniverse: TextView = view.findViewById(R.id.tv_fighter_universe)
        private val cvFighter: CardView = view.findViewById(R.id.cv_fighter)

        fun bind(fighter: Fighter, onClickListener: OnClickListener) {
            tvFighterName.text = fighter.name
            tvFighterUniverse.text = fighter.universe

            Glide.with(ivFighterThumb).load(fighter.imageURL).into(ivFighterThumb)

            cvFighter.setOnClickListener { onClickListener.itemClick(fighter) }
        }
    }

    interface OnClickListener {
        fun itemClick(fighter: Fighter)
    }
}