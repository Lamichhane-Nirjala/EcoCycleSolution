import com.example.ecocyclesolution.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
fun registerUser(
    name: String,
    email: String,
    dob: String,
    password: String
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {

            val uid = auth.currentUser!!.uid

            val user = UserModel(
                userId = uid,
                fullName = name,
                email = email,
                dob = dob,
                profileImageUrl = ""
            )

            db.collection("users")
                .document(uid)
                .set(user)
        }
}