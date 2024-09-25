package com.eoshopping

import ViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eoshopping.repositories.UserRepository
import com.eoshopping.viewmodel.UserViewModel
import java.lang.IllegalArgumentException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tv_name: TextView
    private lateinit var tv_email: TextView
    private lateinit var tv_mobile: TextView
    private lateinit var tv_age: TextView
    private lateinit var iv_profile: ImageView

    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_name = view.findViewById<TextView>(R.id.tv_name)
        tv_email = view.findViewById<TextView>(R.id.tv_email)
        tv_mobile = view.findViewById<TextView>(R.id.tv_mobile)
        tv_age = view.findViewById<TextView>(R.id.tv_age)
        iv_profile = view.findViewById(R.id.iv_profile)



        val fAuth = FirebaseAuth.getInstance()
        val userId = fAuth.currentUser!!.uid
//        val docReference = db.collection("Users").document(userId)

        val repository = UserRepository()

            viewModel = ViewModelProvider(this,ViewModelFactory(repository)).get(UserViewModel::class.java)

            viewModel.users.observe(viewLifecycleOwner,{items->
            tv_name.setText(items.user)
            tv_mobile.setText(items.mobile)
            tv_age.setText(items.age)
            tv_email.setText(items.email)

            val profileRef = FirebaseStorage.getInstance().getReference().child(items.imageUri)
            profileRef.downloadUrl.addOnSuccessListener {uri->
                Picasso.get().load(uri).into(iv_profile)
            }
            /*items.imageUri.let {imageUrl->
                Picasso.get().load(imageUrl).error(R.drawable.ic_launcher_background).into(iv_profile)
            }*/
            /*val profileRef = FirebaseStorage.getInstance().getReference().child("users/${userId}/profile.jpg")
            profileRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(iv_profile)
            }*/

        })

        viewModel.fetchUsers(userId)

//        db.collection("Users").document(userId).get().addOnCompleteListener(OnCompleteListener {
//            if(it.isSuccessful){
//                if(it.result.exists()){
//                    tv_name.setText(it.result.getString("name"))
//                    tv_mobile.setText(it.result.getString("mobile"))
//                    tv_age.setText(it.result.getString("age"))
//                    tv_email.setText(it.result.getString("email"))
//
//                }
//            }
//        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }
}