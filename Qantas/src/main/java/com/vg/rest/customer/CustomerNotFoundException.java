package com.vg.rest.customer;

import com.vg.exception.EntityNotFoundException;

public class CustomerNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = -6170139930481436105L;

    public CustomerNotFoundException(final long id) {
        super(id, "Customer could not be found with id: " + id);
    }

}
