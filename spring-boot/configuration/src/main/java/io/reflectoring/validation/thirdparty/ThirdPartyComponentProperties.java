package io.reflectoring.validation.thirdparty;

import javax.validation.constraints.NotBlank;

/**
 * We assume that this bean comes from another jar file
 */
public class ThirdPartyComponentProperties {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
