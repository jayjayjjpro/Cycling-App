// Generated by view binder compiler. Do not edit!
package com.example.cyclingapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cyclingapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCreateEventBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button createEventButton;

  @NonNull
  public final Button eventDateButton;

  @NonNull
  public final EditText eventLocationInput;

  @NonNull
  public final EditText eventNameInput;

  @NonNull
  public final Button eventTimeButton;

  @NonNull
  public final FrameLayout fragmentContainerCreateEvent;

  @NonNull
  public final Button goBack;

  private ActivityCreateEventBinding(@NonNull ScrollView rootView,
      @NonNull Button createEventButton, @NonNull Button eventDateButton,
      @NonNull EditText eventLocationInput, @NonNull EditText eventNameInput,
      @NonNull Button eventTimeButton, @NonNull FrameLayout fragmentContainerCreateEvent,
      @NonNull Button goBack) {
    this.rootView = rootView;
    this.createEventButton = createEventButton;
    this.eventDateButton = eventDateButton;
    this.eventLocationInput = eventLocationInput;
    this.eventNameInput = eventNameInput;
    this.eventTimeButton = eventTimeButton;
    this.fragmentContainerCreateEvent = fragmentContainerCreateEvent;
    this.goBack = goBack;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreateEventBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreateEventBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_event, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreateEventBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.create_event_button;
      Button createEventButton = ViewBindings.findChildViewById(rootView, id);
      if (createEventButton == null) {
        break missingId;
      }

      id = R.id.event_date_button;
      Button eventDateButton = ViewBindings.findChildViewById(rootView, id);
      if (eventDateButton == null) {
        break missingId;
      }

      id = R.id.event_location_input;
      EditText eventLocationInput = ViewBindings.findChildViewById(rootView, id);
      if (eventLocationInput == null) {
        break missingId;
      }

      id = R.id.event_name_input;
      EditText eventNameInput = ViewBindings.findChildViewById(rootView, id);
      if (eventNameInput == null) {
        break missingId;
      }

      id = R.id.event_time_button;
      Button eventTimeButton = ViewBindings.findChildViewById(rootView, id);
      if (eventTimeButton == null) {
        break missingId;
      }

      id = R.id.fragment_container_create_event;
      FrameLayout fragmentContainerCreateEvent = ViewBindings.findChildViewById(rootView, id);
      if (fragmentContainerCreateEvent == null) {
        break missingId;
      }

      id = R.id.go_back;
      Button goBack = ViewBindings.findChildViewById(rootView, id);
      if (goBack == null) {
        break missingId;
      }

      return new ActivityCreateEventBinding((ScrollView) rootView, createEventButton,
          eventDateButton, eventLocationInput, eventNameInput, eventTimeButton,
          fragmentContainerCreateEvent, goBack);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}