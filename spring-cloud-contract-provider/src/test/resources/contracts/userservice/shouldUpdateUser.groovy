package userservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("When a PUT request with a User is made, the updated user's ID is returned")
    request {
        method 'PUT'
        url '/user-service/users/42'
        body(
                firstName: "Arthur",
                lastName: "Dent"
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body(
                id: 42
        )
        headers {
            contentType(applicationJson())
        }
    }
}