fun main() {

    val com01 = Company("IBM", 2000, "Jack Mezus")
    val com02 = Company("IBM", 2000, "Jack Mezus")

    // copy method
    val com03 = com01.copy("Youtube")

    println(com01)
    println(com03)

    if (com01 == com02) println("EQUAL") else println("NOT EQUAL")
}

