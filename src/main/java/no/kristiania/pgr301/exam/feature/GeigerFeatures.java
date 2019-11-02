package no.kristiania.pgr301.exam.feature;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum GeigerFeatures implements Feature {
  @Label("Possible to name your device")
  NAMING_FEATURE,

  @Label("Possible to set the type of device")
  TYPE_FEATURE;

  public boolean isActive() {
    return FeatureContext.getFeatureManager().isActive(this);
  }
}
