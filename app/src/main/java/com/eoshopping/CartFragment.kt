package com.eoshopping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var productNameEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var productImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var addProductButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = null

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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    /*companion object {
        */
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productNameEditText = view.findViewById(R.id.productNameEditText)
        productPriceEditText = view.findViewById(R.id.productPriceEditText)
        productImageView = view.findViewById(R.id.productImageView)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        addProductButton = view.findViewById(R.id.addProductButton)

        selectImageButton.setOnClickListener { openImagePicker() }
        addProductButton.setOnClickListener { addProduct() }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            // Load the image into ImageView using Picasso
            Picasso.get().load(selectedImageUri).into(productImageView)
            productImageView.visibility = View.VISIBLE
        }
    }

    private fun addProduct() {
        val productName = productNameEditText.text.toString().trim()
        val productPrice = productPriceEditText.text.toString().trim()

        if (productName.isEmpty() || productPrice.isEmpty() || selectedImageUri == null) {
//            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Use a typed HashMap for Firestore
        val product = hashMapOf<String, Any>(
            "name" to productName,
            "price" to productPrice.toDouble(),
            "imageUrl" to "" // Placeholder for imageUrl
        )

        // Upload image to Firebase Storage
        val imageRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                // Get the image URL
                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    product["imageUrl"] = imageUrl.toString() // Update the imageUrl
                    saveProductToFirestore(product)
                }
            }
            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProductToFirestore(product: HashMap<String, Any>) {
        firestore.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
//                Toast.makeText(this, "Product added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error adding product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        productNameEditText.text.clear()
        productPriceEditText.text.clear()
        productImageView.setImageURI(null)
        productImageView.visibility = View.GONE
        selectedImageUri = null
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }


}