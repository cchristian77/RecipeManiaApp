package com.example.recipemaniaapp.ui.newrecipe

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.recipemaniaapp.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo_form.*
import java.util.*

class PhotoFormFragment: Fragment(), View.OnClickListener {

    lateinit var alertDialog:AlertDialog
    lateinit var storageReference:StorageReference

    lateinit var tvTitle: TextView
    lateinit var tvFormDescription: TextView
    lateinit var saveBtn : Button

    companion object {
        var EXTRA_TITLE = "extra_title"
        var EXTRA_FORM_DESC = "extra_form_desc"
        var EXTRA_BUTTON = "extra_button"
        private val PICK_IMAGE_CODE=1000
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveBtn = view.findViewById(R.id.save_btn)
        var backBtn : LinearLayout = view.findViewById(R.id.back_btn)
        saveBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)

        tvTitle = view.findViewById(R.id.tv_title)
        tvFormDescription = view.findViewById(R.id.tv_form_desc)
        saveBtn = view.findViewById(R.id.save_btn)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments != null) {
            tvTitle.text = arguments?.getString(AddFormFragment.EXTRA_TITLE)
            tvFormDescription.text = arguments?.getString(AddFormFragment.EXTRA_FORM_DESC)
            saveBtn.setText(arguments?.getString(AddFormFragment.EXTRA_BUTTON))

        }

    }

    override fun onClick(v: View) {
        if(v.id == R.id.save_btn) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
        }
        val fragmentManager = activity!!.supportFragmentManager
        fragmentManager.popBackStack("Form", 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val fragmentManager = activity!!.supportFragmentManager
                if(fragmentManager.backStackEntryCount > 0)
                    fragmentManager.popBackStack("Form", 1)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

//        alertDialog = SpotsDialog.Builder().setContext(activity).build()
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_CODE) {
            if(data == null || data.data == null) return

            val filePath = data.data

            if(filePath != null) {
                val ref = storageReference.child("recipe_image/" + UUID.randomUUID().toString())
                val uploadTask = ref?.putFile(filePath!!)

                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            Toast.makeText(activity,"Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri!!.toString()
                        Log.d("DIRECT LINK", url)
                        Picasso.get().load(data.data).into(img_preview)
                    }
                }
            }else{
                Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }
        }
//            alertDialog.show()
//            val uploadTask = storageReference!!.putFile(data!!.data!!)
//            val task = uploadTask.continueWithTask {
//                task ->
//                if(!task.isSuccessful) {
//                    Toast.makeText(activity,"Failed", Toast.LENGTH_SHORT).show()
//                }
//                storageReference!!.downloadUrl
//            }.addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    val downloadUri = task.result
//                    val url = downloadUri!!.toString()
//                    Log.d("DIRECT LINK", url)
//                    alertDialog.dismiss()
//                    Picasso.get().load(url).into(image_preview)
//                }
//            }

    }

}



