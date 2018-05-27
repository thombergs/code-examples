package userservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("When a POST request with a User is made, the created user's ID is returned")
    request {
        method 'POST'
        url '/user-service/users'
        body(
                firstName: "Arthur",
                lastName: "Dent"
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 201
        body(
                id: 42
        )
        headers {
            contentType(applicationJson())
        }
    }
}