package at.favre.lib.hood.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import at.favre.lib.hood.interfaces.actions.BoolConfigAction;
import at.favre.lib.hood.interfaces.actions.ButtonDefinition;
import at.favre.lib.hood.interfaces.actions.ManagerControl;
import at.favre.lib.hood.interfaces.actions.OnClickAction;
import at.favre.lib.hood.interfaces.actions.SingleSelectListConfigAction;
import at.favre.lib.hood.interfaces.values.DynamicValue;

/**
 * The main API for the lib. Responsible for creating {@link Pages} and {@link PageEntry}
 */
public interface HoodAPI {
    /**
     * Creates a new {@link Pages} object that can be used to fill with {@link Page}
     *
     * @param config the config used to create the pages
     * @return pages
     */
    @NonNull
    Pages createPages(@NonNull Config config);

    /**
     * Single column action
     *
     * @param action
     */
    PageEntry<?> createActionEntry(ButtonDefinition action);

    /**
     * Two columns with 2 different actions in a row
     *
     * @param actionLeft
     * @param actionRight
     */
    PageEntry<?> createActionEntry(ButtonDefinition actionLeft, ButtonDefinition actionRight);

    /**
     * Creates a header entry
     *
     * @param header as shown in ui
     */
    PageEntry<?> createHeaderEntry(CharSequence header);

    /**
     * Creates a header entry
     *
     * @param header    as shown in ui
     * @param hideInLog will omit it in log
     */
    PageEntry<?> createHeaderEntry(CharSequence header, boolean hideInLog);

    /**
     * Creates a simple, non-interactive text message
     *
     * @param message as shown in
     */
    PageEntry<?> createMessageEntry(CharSequence message);

    /**
     * Creates an interactive switch like entry
     *
     * @param action
     */
    PageEntry<?> createSwitchEntry(BoolConfigAction action);

    /**
     * Creates a single-select from list entry (ie. spinner or drop-down list)
     *
     * @param action
     */
    PageEntry<?> createSpinnerEntry(SingleSelectListConfigAction action);

    /**
     * Creates Key-Value style page entry.
     *
     * @param key       as shown in ui
     * @param value     dynamic value (e.g. from {@link android.content.SharedPreferences}
     * @param action    used when clicked on
     * @param multiLine if a different layout should be used for long values
     */
    PageEntry<?> createPropertyEntry(CharSequence key, DynamicValue<String> value, OnClickAction action, boolean multiLine);

    /**
     * Creates Key-Value style page entry. Uses dialog as default click action.
     *
     * @param key       as shown in ui
     * @param value     dynamic value (e.g. from {@link android.content.SharedPreferences}
     * @param multiLine if a different layout should be used for long values
     */
    PageEntry<?> createPropertyEntry(CharSequence key, DynamicValue<String> value, boolean multiLine);

    /**
     * Creates Key-Value style page entry. Uses dialog as default click action and is not
     * multiline enabled.
     *
     * @param key   as shown in ui
     * @param value dynamic value (e.g. from {@link android.content.SharedPreferences}
     */
    PageEntry<?> createPropertyEntry(CharSequence key, DynamicValue<String> value);

    /**
     * Creates Key-Value style page entry with a static value.
     *
     * @param key       as shown in ui
     * @param value     static value
     * @param action    used when clicked on (see {@link HoodAPI.Extension} createOnClickAction*() methods
     * @param multiLine if a different layout should be used for long values
     */
    PageEntry<?> createPropertyEntry(CharSequence key, String value, OnClickAction action, boolean multiLine);

    /**
     * Creates Key-Value style page entry with a static value. Uses dialog as default click action.
     *
     * @param key       as shown in ui
     * @param value     static value
     * @param multiLine if a different layout should be used for long values
     */
    PageEntry<?> createPropertyEntry(CharSequence key, String value, boolean multiLine);

    /**
     * Creates Key-Value style page entry with a static value. Uses dialog as default click action and
     * ist not multi-line enabled.
     *
     * @param key   as shown in ui
     * @param value static value
     */
    PageEntry<?> createPropertyEntry(CharSequence key, String value);

    /**
     * Creates a simple non interactive element displaying given message
     *
     * @param message to display
     */
    PageEntry<?> createSimpleMessageEntry(@NonNull CharSequence message);

    /**
     * Extension of the API to allow creation of {@link Section} and {@link OnClickAction} for {@link PageEntry}
     */
    interface Extension {
        /**
         * Creates an empty {@link Section} with given header element
         *
         * @param header of this section
         * @return section
         */
        Section.ModifiableHeaderSection createSection(String header);

        /**
         * Creates an section with given header an page entry elements
         *
         * @param header  of this section
         * @param entries added to this section
         * @return section
         */
        Section.ModifiableHeaderSection createSection(String header, @NonNull List<PageEntry<?>> entries);

        /**
         * Creates an {@link OnClickAction} used mainly for {@link #createPropertyEntry} type of entries.
         * This click action will open an runtime permission dialog if given permission was not granted or
         * the app info if it was or is blocked.
         *
         * @param androidPermission the permission as string, as defined in AndroidManifest.xml e.g. "android.permission.BLUETOOTH"
         * @param activity          the current activity as context
         * @return the click action
         */
        OnClickAction createOnClickActionAskPermission(String androidPermission, Activity activity);

        /**
         * Creates an {@link OnClickAction} used mainly for {@link #createPropertyEntry} type of entries.
         * This click action will start an activity with provided intent.
         * @param intent will be used to start an activity
         * @return the click action
         */
        OnClickAction createOnClickActionStartIntent(Intent intent);

        /**
         * Creates an {@link OnClickAction} used mainly for {@link #createPropertyEntry} type of entries.
         * This click action will show a toast message properties of the {@link PageEntry}
         * @return the click action
         */
        OnClickAction createOnClickActionToast();

        /**
         * Creates an {@link OnClickAction} used mainly for {@link #createPropertyEntry} type of entries.
         * This click action will show a dialog with properties of the {@link PageEntry}
         *
         * @return the click action
         */
        OnClickAction createOnClickActionDialog();

        /**
         * Creates a label that can be used {@link #createPropertyEntry} type of entries.
         * @param shortLabel will be shown in the overview
         * @param fullLabel will be shown in a detail view (use {@link #createOnClickActionDialog()}
         * @return the label as char sequence
         */
        CharSequence createFullLabel(CharSequence shortLabel, CharSequence fullLabel);

        /**
         * Creates a shake listener that will start the activity for given intent. Must be started and
         * stopped manually with returned control object.
         *
         * @param ctx
         * @param intent to start when a shake is detected
         * @return control to start an stop the shake detector
         */
        ManagerControl registerShakeToOpenDebugActivity(Context ctx, Intent intent);

        /**
         * A click listener for arbitrary sequences of taps (ie. for double-tap). Will not block set {@link android.view.View.OnClickListener}
         * or {@link android.view.View.OnLongClickListener} on a view. Used with {@link View#setOnTouchListener(View.OnTouchListener)} of a view.
         *
         * Can be used to support "secret" tap method on semi-prominent views in your app to show a debug view. (e.g. triple tap on a text view)
         *
         * @param numOfTaps required to fire the underlying onClickListener
         * @param onClickListener will be called if the tap sequence was successful
         * @return the listener
         */
        View.OnTouchListener createArbitraryTapListener(int numOfTaps, @NonNull View.OnClickListener onClickListener);
    }
}