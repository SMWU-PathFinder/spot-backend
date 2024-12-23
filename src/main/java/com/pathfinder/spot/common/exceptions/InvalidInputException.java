package com.pathfinder.spot.common.exceptions;

import com.pathfinder.spot.common.constants.ExceptionCode;
import com.pathfinder.spot.common.exceptions.BadRequestException;

public class InvalidInputException extends BadRequestException {
    public InvalidInputException(ExceptionCode code) {
        super(code);
    }
}
