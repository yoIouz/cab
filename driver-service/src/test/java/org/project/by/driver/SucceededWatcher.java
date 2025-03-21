package org.project.by.driver;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

@Slf4j
public class SucceededWatcher implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("⚠️Test {} disabled", context.getDisplayName());
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("✅Test {} passed", context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("⚠️Test {} aborted", context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.info("❌Test {} failed", context.getDisplayName());
    }

}
