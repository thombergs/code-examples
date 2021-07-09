fun main() {
    val company = Company("IBM", 1993, "Yann Pierre")
    company.founder = "Achraf Amellal"

    val company_02 = Company("IBM", 1993)
    println(company_02.toString())

    println("the length of this variable is ${company.name.length}")
}