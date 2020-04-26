package com.example.recipemaniaapp.ui.newrecipe

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.recipemaniaapp.R
import com.example.recipemaniaapp.model.Recipe
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_new_recipe.*
import kotlinx.android.synthetic.main.fragment_photo_form.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class NewRecipeFragment : Fragment(), View.OnClickListener {

    lateinit var storageReference: StorageReference
    lateinit var dataPhoto : Uri
    lateinit var urlPhoto : String

    companion object {

        val EXTRA_NAME = "extra_name"
        val EXTRA_INFO = "extra_info"
        val EXTRA_INGREDIENT = "extra_ingredient"
        val EXTRA_STEP = "extra_step"
        val EXTRA_CATEGORY = "extra_category"
        val EXTRA_PHOTO  = "extra_photo"
        private val PICK_IMAGE_CODE = 1000

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnInformation: Button = view.findViewById(R.id.add_information_btn)
        val btnIngredient: Button = view.findViewById(R.id.add_ingredient_btn)
        val btnStep: Button = view.findViewById(R.id.add_steps_btn)
        val btnPhoto: Button = view.findViewById(R.id.add_photos_btn)
        val btnAddRecipe: Button = view.findViewById(R.id.add_new_recipe_btn)

        btnInformation.setOnClickListener(this)
        btnIngredient.setOnClickListener(this)
        btnStep.setOnClickListener(this)
        btnPhoto.setOnClickListener(this)
        btnAddRecipe.setOnClickListener(this)

        checker()

        progressBar.visibility = View.GONE

    }



    override fun onClick(v: View) {

        if (v.id== R.id.add_information_btn || v.id == R.id.add_photos_btn || v.id == R.id.add_ingredient_btn
            || v.id == R.id.add_ingredient_btn || v.id == R.id.add_steps_btn
        ) {
            val fm = activity!!.supportFragmentManager
            val mBundle = Bundle()
            when (v.id) {
                R.id.add_photos_btn -> {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        NewRecipeFragment.PICK_IMAGE_CODE)
                    return

                }

                R.id.add_information_btn -> {

                    mBundle.putString(AddFormFragment.EXTRA_TITLE, "Add Information")
                    mBundle.putString(
                        AddFormFragment.EXTRA_FORM_DESC,
                        "Add food's information such as food description, " +
                                "nutrition fact, serving time, etc."
                    )
                    mBundle.putString(AddFormFragment.EXTRA_WRITE, "Write Information")
                    mBundle.putString(AddFormFragment.EXTRA_BUTTON, "Save Information")

                }

                R.id.add_ingredient_btn -> {
                    mBundle.putString(AddFormFragment.EXTRA_TITLE, "Add Ingredient")
                    mBundle.putString(
                        AddFormFragment.EXTRA_FORM_DESC,
                        "Add the ingredient needed for the food recipe."
                    )
                    mBundle.putString(AddFormFragment.EXTRA_WRITE, "Write Ingredient")
                    mBundle.putString(AddFormFragment.EXTRA_BUTTON, "Save Ingredient")


                }

                R.id.add_steps_btn -> {
                    mBundle.putString(AddFormFragment.EXTRA_TITLE, "Add Steps")
                    mBundle.putString(
                        AddFormFragment.EXTRA_FORM_DESC,
                        "Add dan describe food's preparation step. "
                    )
                    mBundle.putString(AddFormFragment.EXTRA_WRITE, "Write Steps")
                    mBundle.putString(AddFormFragment.EXTRA_BUTTON, "Save Steps")

                }
            }
            mBundle.putString(AddFormFragment.EXTRA_NAME, edt_recipe_name.text.toString())
            mBundle.putString(AddFormFragment.EXTRA_INFO, arguments?.getString(EXTRA_INFO))
            mBundle.putString(AddFormFragment.EXTRA_INGREDIENT, arguments?.getString(EXTRA_INGREDIENT))
            mBundle.putString(AddFormFragment.EXTRA_STEP, arguments?.getString(EXTRA_STEP))
            mBundle.putString(AddFormFragment.EXTRA_CATEGORY, category_spinner.selectedItemId.toString())

            if(this::dataPhoto.isInitialized) {
                mBundle.putString(AddFormFragment.EXTRA_PHOTO, dataPhoto.toString())
            } else {
                mBundle.putString(AddFormFragment.EXTRA_PHOTO, arguments?.getString(EXTRA_PHOTO))
            }


            val mAddFormFragment = AddFormFragment()
            fm
                .beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(
                    R.id.frame_container,
                    mAddFormFragment,
                    AddFormFragment::class.java.simpleName
                )
                .commit()

            mAddFormFragment.arguments = mBundle
        } else if (v.id== R.id.add_new_recipe_btn) {
            validator()
        }
    }

    private fun checker() {
        val strName =  arguments?.getString(EXTRA_NAME)
        val strInfo = arguments?.getString(EXTRA_INFO)
        val strIngredient = arguments?.getString(EXTRA_INGREDIENT)
        val strStep = arguments?.getString(EXTRA_STEP)
        val strCategory = arguments?.getString(EXTRA_CATEGORY)
        val strPhoto = arguments?.getString(EXTRA_PHOTO)

        if(strInfo != null) {
            check_info_img.setImageResource(R.drawable.icon_check)
            add_information_btn.setText("Edit Information")
        }
        if(strIngredient != null) {
            check_ingredient_img.setImageResource(R.drawable.icon_check)
            add_ingredient_btn.setText("Edit Ingredient")
        }
        if(strStep != null) {
            check_how_to_cook_img.setImageResource(R.drawable.icon_check)
            add_steps_btn.setText("Edit Steps")
        }

        if(strCategory != null) {
            val category = strCategory.toInt()
            category_spinner.setSelection(category)
        }

        if(strPhoto != null) {
            tv_preview.setText("Preview")
            add_photos_btn.setText("Change Photo")
            check_photo_img.setImageResource(R.drawable.icon_check)
            val param = image_preview.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,0,0,30)
            image_preview.layoutParams = param
            Glide.with(this).load(strPhoto).into(image_preview)
            val uri = Uri.parse(strPhoto)
            dataPhoto = uri
        }

        if(strName != null) edt_recipe_name.setText(strName)

    }

    private fun validator() {
        var valid: Boolean = true
        val strName =  arguments?.getString(EXTRA_NAME)
        val strInfo = arguments?.getString(EXTRA_INFO)
        val strIngredient = arguments?.getString(EXTRA_INGREDIENT)
        val strStep = arguments?.getString(EXTRA_STEP)
        val strPhoto = arguments?.getString(EXTRA_PHOTO)

        if(strInfo == null || strIngredient == null || strStep == null) {
            valid = false
        } else {
            val recipeName = edt_recipe_name.text.toString().trim()
            if(recipeName.isEmpty()) {
                edt_recipe_name.error = "Recipe's name cannot be empty."
                valid = false
            }

            if(category_spinner.selectedItemId.toInt() == 0) valid = false

            if(!this::dataPhoto.isInitialized && strPhoto == null) valid=false
            }

        if(valid == false) Toast.makeText(activity,"Your Recipe is not complete.", Toast.LENGTH_SHORT).show()
        else {
            progressBar.visibility = View.VISIBLE
            uploadPhoto()
        }

    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ").trim()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == NewRecipeFragment.PICK_IMAGE_CODE) {
            if(data == null || data.data == null) return

            val filePath = data!!.data

            if(filePath != null) dataPhoto = filePath

            if(filePath != null) {
                tv_preview.setText("Preview")
                add_photos_btn.setText("Change Photo")

                check_photo_img.setImageResource(R.drawable.icon_check)
                val param = image_preview.layoutParams as LinearLayout.LayoutParams
                param.setMargins(0,0,0,30)
                image_preview.layoutParams = param

                Glide.with(this).load(dataPhoto).into(image_preview)
            }else{
                Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadPhoto() {

        var url: String? = null
        storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference.child("recipe_image/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(dataPhoto)

        val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Toast.makeText(activity,"Failed", Toast.LENGTH_SHORT).show()
                }
            }
            return@Continuation ref.downloadUrl
        })?.addOnCompleteListener {
                task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()
                    uploadToDatabase(url)
                    Log.d("DIRECT LINK", url)
                }
        }

    }

    private fun uploadToDatabase(url: String) {

        val databaseRef = FirebaseDatabase.getInstance().getReference("Recipe")
        val recipeName = edt_recipe_name.text.toString().capitalizeWords()
        val user = GoogleSignIn.getLastSignedInAccount(activity)
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
        val recipeID = databaseRef.push().key.toString()
        val recipe = Recipe(recipeID, edt_recipe_name.text.toString(),  recipeName, arguments?.getString(EXTRA_INGREDIENT),
            arguments?.getString(EXTRA_STEP), category_spinner.selectedItem.toString(), user?.email.toString(),
            url, time)

        databaseRef.child(recipeID).setValue(recipe)
            .addOnCompleteListener {
                Toast.makeText(activity, "$recipeName recipe added successfully.",Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(activity, "$recipeName saved failed.",Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }

}
