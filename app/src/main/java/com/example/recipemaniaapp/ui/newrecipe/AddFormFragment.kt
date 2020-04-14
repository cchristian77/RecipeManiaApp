package com.example.recipemaniaapp.ui.newrecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.recipemaniaapp.R

class AddFormFragment : Fragment(), View.OnClickListener{

    lateinit var tvTitle: TextView
    lateinit var tvFormDescription: TextView
    lateinit var tvWrite : TextView
    lateinit var edtInput : EditText
    lateinit var saveBtn : Button

    companion object {
        var EXTRA_TITLE = "extra_title"
        var EXTRA_FORM_DESC = "extra_form_desc"
        var EXTRA_BUTTON = "extra_button"
        var EXTRA_WRITE = "extra_write"

        val EXTRA_NAME = "extra_name"
        val EXTRA_INFO = "extra_info"
        val EXTRA_INGREDIENT = "extra_ingredient"
        val EXTRA_STEP = "extra_step"
        val EXTRA_CATEGORY = "extra_category"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveBtn = view.findViewById(R.id.save_btn)
        var backBtn: LinearLayout = view.findViewById(R.id.back_btn)
        saveBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)


        tvTitle = view.findViewById(R.id.tv_title)
        tvFormDescription = view.findViewById(R.id.tv_form_desc)
        tvWrite = view.findViewById(R.id.tv_write)
        saveBtn = view.findViewById(R.id.save_btn)
        edtInput = view.findViewById(R.id.edt_input)

        edtInput.setHint("Input in this text field.")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments != null) {
            tvTitle.text = arguments?.getString(EXTRA_TITLE)
            tvFormDescription.text = arguments?.getString(EXTRA_FORM_DESC)
            tvWrite.text = arguments?.getString(EXTRA_WRITE)
            saveBtn.setText(arguments?.getString(EXTRA_BUTTON))

        }

        checker()

    }

    override fun onClick(v: View) {
        val mBundle = Bundle()
        val fm = activity!!.supportFragmentManager
        val mNewRecipeFragment = NewRecipeFragment()
        if(v.id == R.id.save_btn) {
            val checker = edtInput.text.toString().trim()
            if(checker.isEmpty()) {
                edtInput.error = "The input cannot be empty"
                return
            } else {


                if(tvTitle.text == "Add Information") {
                    mBundle.putString(NewRecipeFragment.EXTRA_INFO, edtInput.text.toString())
                    mBundle.putString(NewRecipeFragment.EXTRA_INGREDIENT, arguments?.getString(EXTRA_INGREDIENT))
                    mBundle.putString(NewRecipeFragment.EXTRA_STEP, arguments?.getString(EXTRA_STEP))

                } else if (tvTitle.text == "Add Ingredient") {
                    mBundle.putString(NewRecipeFragment.EXTRA_INGREDIENT, edtInput.text.toString())
                    mBundle.putString(NewRecipeFragment.EXTRA_INFO, arguments?.getString(EXTRA_INFO))
                    mBundle.putString(NewRecipeFragment.EXTRA_STEP, arguments?.getString(EXTRA_STEP))
                } else if (tvTitle.text== "Add Steps") {
                    mBundle.putString(NewRecipeFragment.EXTRA_STEP, edtInput.text.toString())
                    mBundle.putString(NewRecipeFragment.EXTRA_INFO, arguments?.getString(EXTRA_INFO))
                    mBundle.putString(NewRecipeFragment.EXTRA_INGREDIENT, arguments?.getString(EXTRA_INGREDIENT))
                }

                mBundle.putString(NewRecipeFragment.EXTRA_CATEGORY, arguments?.getString(EXTRA_CATEGORY))
                mBundle.putString(NewRecipeFragment.EXTRA_NAME, arguments?.getString(EXTRA_NAME))

                fm
                    .beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(
                        R.id.frame_container,
                        mNewRecipeFragment,
                        NewRecipeFragment::class.java.simpleName
                    )
                    .addToBackStack("Form")
                    .commit()

                mNewRecipeFragment.arguments = mBundle

                return
            }

        }
        mBundle.putString(NewRecipeFragment.EXTRA_INFO, arguments?.getString(EXTRA_INFO))
        mBundle.putString(NewRecipeFragment.EXTRA_INGREDIENT, arguments?.getString(EXTRA_INGREDIENT))
        mBundle.putString(NewRecipeFragment.EXTRA_INGREDIENT, arguments?.getString(EXTRA_INGREDIENT))
        mBundle.putString(NewRecipeFragment.EXTRA_CATEGORY, arguments?.getString(EXTRA_CATEGORY))
        mBundle.putString(NewRecipeFragment.EXTRA_NAME, arguments?.getString(EXTRA_NAME))
        val fragmentManager = activity!!.supportFragmentManager
        fragmentManager.popBackStack("Form", 1)
        fm
            .beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(
                R.id.frame_container,
                mNewRecipeFragment,
                NewRecipeFragment::class.java.simpleName
            )
            .addToBackStack("Form")
            .commit()

        mNewRecipeFragment.arguments = mBundle
//        fragmentManager.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                val fragmentManager = activity!!.supportFragmentManager
//
//                if(fragmentManager.backStackEntryCount  > 0)
//                    fragmentManager.popBackStack("Form", 1)
//
//
//                }
//
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

   private fun checker() {
        val strInfo = arguments?.getString(EXTRA_INFO)
        val strIngredient = arguments?.getString(EXTRA_INGREDIENT)
        val strStep = arguments?.getString(EXTRA_STEP)

        if (strInfo != null || strIngredient != null || strStep != null) {
            if (tvTitle.text == "Add Information") {
                edtInput.setText(strInfo)
            } else if (tvTitle.text == "Add Ingredient") {
                edtInput.setText(strIngredient)
            } else if (tvTitle.text == "Add Steps") {
                edtInput.setText(strStep)
            }
        }


    }
}

