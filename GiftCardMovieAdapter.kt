package com.demomvvm.MVVM.GiftCard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demomvvm.R
import com.demomvvm.databinding.MovieLayoutBinding
import com.squareup.picasso.Picasso


/*
1.load data from api
2.save in room

1.load data from room
2.load new data from api
3.save in room
 */
class GiftCardMovieAdapter : RecyclerView.Adapter<GiftCardMovieAdapter.ViewHolder>() {
    val TAG = "Myy GiftCardMovieAdapter ";
    
    private var movieList = ArrayList<GiftCardResult>()
    private var checkClickEnable = false

    fun setMovieList(movieList: List<GiftCardResult>, checkClickEnable:Boolean) {
        this.movieList = movieList as ArrayList<GiftCardResult>
        this.checkClickEnable = checkClickEnable //for not touch any view of room data , until new data load from api
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: MovieLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var ecardPojo1 = movieList[position]

        //without use of holder every time
        /*with(holder){
            /* with(ecardPojo1){
                    binding.tvLangName.text = this.ecard_name
                    binding.tvExp.text = this.ecard_id
                }*/
            binding.movieName.text = ecardPojo1.ecard_name //use without of findviewbyid
        }*/



//        Picasso.get()
//                .load(afterDecode)
//                .placeholder(R.drawable.img_not_available)
//                .error(R.drawable.img_not_available)
//                .into(imageView);


        Picasso.get()
            .load("https://www.wemad.com.au/upload/ecards/" + ecardPojo1.ecard_image)
            .placeholder(R.drawable.img_not_available)
            .error(R.drawable.img_not_available)
            .into(holder.binding.movieImage)

      /*  Glide.with(container.getContext())
            .load(url_str)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(imageView)*/

        holder.binding.movieName.text = ecardPojo1.ecard_name //use without of findviewbyid

        holder.itemView.setOnClickListener {
            if(checkClickEnable){
                Log.i(TAG, "click is Enable for ecardPojo1.ecard_name = "+ecardPojo1.ecard_name)
            }
            else{
                Log.i(TAG, "click is Disable for ecardPojo1.ecard_name = "+ecardPojo1.ecard_name)
            }
        }

    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}