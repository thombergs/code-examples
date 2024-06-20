fun main() {
    val company = Company(null, 1993, "Yann Piette")
    val result = company.name?.length ?: "name property is null"
    println(" ${result}")

    val firstName = "Achraf Amellal"
    println(firstName)


}