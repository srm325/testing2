package com.aliakberaakash.cutiehacksproject2020.ui.features.uploadImage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.aliakberaakash.cutiehacksproject2020.R
import com.aliakberaakash.cutiehacksproject2020.data.model.Post
import com.aliakberaakash.cutiehacksproject2020.data.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.searchsong_layout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CountDownLatch

private const val BASE_URL =
    "https://www.googleapis.com/youtube/v3/search?key=AIzaSyAcqsfnhOFIyIgX1auWR-SzjcTXOoc3MDE&part=snippet,id&order=viewCount&maxResults=1&q="

class UploadImageFragment : Fragment() {
    companion object{
        const val GET_FROM_GALLERY = 3
    }
    var bitmap: Bitmap? = null
    lateinit var videostring: String
    lateinit var selectedImage:Uri
    lateinit var storage: FirebaseStorage
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.searchsong_layout, container, false)

        val sendBtn: MaterialButton = view.findViewById(R.id.button2)
        val uploadBtn: MaterialButton = view.findViewById(R.id.uploadBtn)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        storage = Firebase.storage

        val videostring = (EditText) findViewById(R.id.imageUpload);

        uploadBtn.setOnClickListener {
            val searchtext: String = videostring.getText().toString()

            val youTubePlayerView: YouTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
            lifecycle.addObserver(youTubePlayerView)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videolink = requestData(BASE_URL + searchtext)
                    var videojson: JSONObject? = null
                    var video: String? = null
                    var videoarray: JSONArray? = null
                    var videoarray1: JSONObject? = null
                    try {
                        videojson = JSONObject(videolink)
                        videoarray = videojson.getJSONArray("items")
                        videoarray1 = videoarray.getJSONObject(0).getJSONObject("id")
                        video = videoarray1.getString("videoId")
                        Log.d("FUCKYOU", videoarray1.getString("videoId"))
                    } catch (e: JSONException) {
                        Log.d("ONREADY", e.message!!)
                        e.printStackTrace()
                    }
                    youTubePlayer.loadVideo(video!!, 0f)


                }
            })
            uploadBtn.setOnClickListener {

                val post = Post(
                    id = "",
                    user = User(
                        Firebase.auth.currentUser!!.displayName!!,
                        ""
                    ),
                    description = "",
                    image =""/* imagesRef.path*/
                )
            /*
            if(bitmap==null){
                Toast.makeText(requireContext(), "Choose an image", Toast.LENGTH_SHORT).show()
            }else{
                main_group.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var storageRef = storage.reference
                val currentTime = Timestamp(System.currentTimeMillis())
                val filename = "$currentTime.jpg"
                val imagesRef = storageRef.child("images/$filename")
                var uploadTask = imagesRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->

                    val post = Post(
                        id = "",
                        user = User(
                            Firebase.auth.currentUser!!.displayName!!,
                            ""
                        ),
                        description = "",
                        image = imagesRef.path
                    )
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    db.collection("posts")
                        .add(post)
                        .addOnSuccessListener { documentReference ->
                            main_group.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            bitmap = null
                            imageUpload.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.img_sub)
                            )

                            //set the post id to reference it later
                            post.id=documentReference.id
                            db.collection("posts").document(documentReference.id)
                                .set(post)

                            Toast.makeText(
                                requireContext(),
                                "Successfully Uploaded",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->

                        }

                     */
                }
            }


        }

    }
private fun requestData(urlstring: String): String? {
    return try {
        val response = arrayOfNulls<String>(1)
        val latch = CountDownLatch(1)
        Thread {
            try {
                Log.d("START", "Starting GET")
                val url = URL(urlstring)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()
                Log.d("INFO", urlstring)
                Log.d("INFO", Integer.toString(connection.responseCode))
                Log.d("INFO", connection.responseMessage)
                val rd = BufferedReader(InputStreamReader(connection.inputStream))
                var content = ""
                var line: String
                while (rd.readLine().also { line = it } != null) {
                    content += """
                        $line
                        
                        """.trimIndent()
                }
                response[0] = content
                Log.d("SUCCESS", response[0]!!)
                latch.countDown()
            } catch (ex: Exception) {
                Log.d("ERROR", "Error Processing Get Request...")
                var i = 0
                while (i < ex.stackTrace.size) {
                    Log.d("ERROR", ex.stackTrace[i].toString())
                    i++
                }
                latch.countDown()
            }
        }.start()
        latch.await()
        response[0]
    } catch (ex: Exception) {
        ""
    }
}
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        //Detects request codes
        /*
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImage = data.data!!
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
                imageUpload.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}*/