package com.example.myscope.adapter

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myscope.ImageLoader
import com.example.myscope.ImageType
import com.example.myscope.R
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.article.PostArticleFragment
import com.example.myscope.manager.DialogManager
import com.example.myscope.manager.article.Article
import com.example.myscope.manager.article.ArticleManager
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class ArticleAdapter(context: Context, articles: ArrayList<Article>,
                     private val uid: String) :
    ArrayAdapter<Article>(context, R.layout.item_friend, articles) {

    private lateinit var holder: ViewHolder
    private val c: Calendar = Calendar.getInstance()

    private class ViewHolder(v: View) {
        val img_avatar: ImageView = v.findViewById(R.id.img_avatar)
        val tv_name: TextView = v.findViewById(R.id.tv_name)
        val tv_time: TextView = v.findViewById(R.id.tv_time)
        val img_more: ImageView = v.findViewById(R.id.img_more)
        val tv_content: TextView = v.findViewById(R.id.tv_content)
        val tv_tag: TextView = v.findViewById(R.id.tv_tag)
        val img_photo: ImageView = v.findViewById(R.id.img_photo)
        val tv_like: TextView = v.findViewById(R.id.tv_like)
        val tv_comment: TextView = v.findViewById(R.id.tv_comment)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View
        val item = getItem(position)

        if(convertView == null){
            view = View.inflate(context, R.layout.item_article,null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        ImageLoader.loadImage(holder.img_avatar, item.avatar)
        ImageLoader.loadImage(holder.img_photo, item.photo, ImageType.Article)
        holder.tv_name.text = item.name

        c.timeInMillis = item.timestamp
        holder.tv_time.text = String.format("%d/%02d/%02d", c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
        holder.img_more.visibility = if (item.uid == uid) View.VISIBLE else View.GONE
        //holder.tv_comment.text = "${viewGroup.resources.getString(R.string.comment)}(${getItem(position).messageTotal})"

        showContent(position)
        setListen(position)

        return view
    }

    private fun showContent(position: Int) {
        if (getItem(position).content.isEmpty()) {
            holder.tv_content.visibility = View.GONE
        } else {
            var content = getItem(position).content
            val sb = StringBuilder(content)
            val ssb = SpannableStringBuilder("")
            val regex = Regex("""(#+\S*\s)""")

            content = content.replace('＃', '#')
            Linkify.addLinks(holder.tv_content, Linkify.EMAIL_ADDRESSES or Linkify.WEB_URLS)

            if (regex.findAll(content).count()>0) {
                var counter = 0
                var prefix = 0

                regex.findAll(content).forEach {
                    content = sb.insert(it.range.endInclusive+counter, " ").toString()
                    ssb.append(content.substring(prefix, it.range.endInclusive+counter+1))

                    ssb.setSpan(object : ClickableSpan(){
                        override fun onClick(widget: View) {
                            val b = Bundle()
                            b.putString("CueTag", it.value.dropLast(1).replace("#",""))
                            //TODO("只顯示相同TAG")
                        }
                    }, it.range.start+counter, it.range.endInclusive+counter, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    prefix = it.range.endInclusive+counter+1
                    counter =+ 1
                }
                ssb.append(sb.substring(prefix, sb.length))
            } else
                ssb.append(sb)

            holder.tv_content.text = ssb
            holder.tv_content.visibility = View.VISIBLE
            holder.tv_content.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setListen(position: Int) {
        holder.img_more.setOnClickListener {
            val option = arrayOf("編輯", "刪除")
            val mActivity = it.context as MainActivity
            DialogManager.instance.showList(mActivity, option)
                ?.setOnItemClickListener { parent, view, i, id ->
                    DialogManager.instance.dismissDialog()
                    when(i){
                        0 -> {
                            val b = Bundle()
                            b.putInt("Mode", 1)
                            b.putSerializable("Article", getItem(position))
                            mActivity.switchTo(PostArticleFragment(), b)
                            mActivity.showNavigationDrawer(false)
                            mActivity.showNavigationBottom(false)
                        }
                        1 -> ArticleManager.instance.deleteArticle(getItem(position).id)
                    }
                }
        }
    }
}