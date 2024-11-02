package com.capgemini.wsb.fitnesstracker.user.internal;

import org.springframework.lang.Nullable;

public record UserBasicDto(@Nullable Long id, @Nullable String nickname) {
}
