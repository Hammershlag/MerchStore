package com.example.merchstore.components.interfaces;

/**
 * The DataDisplay interface provides methods for manipulating the display of data.
 * It is designed to control what data users see. For instance, if certain data should not be visible to users (like IDs),
 * these methods can be implemented to change or hide this data.
 *
 * It has two methods:
 * <ul>
 *     <li>{@link #displayData()}: This method is used to display the data. The implementation of this method should handle the logic of what data is to be displayed.</li>
 *     <li>{@link #limitedDisplayData()}: This method is used to display a limited set of data. The implementation of this method should handle the logic of what limited data is to be displayed.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 30.05.2024
 */
public interface DataDisplay {
    /**
     * Display the data. The implementation of this method should handle the logic of what data is to be displayed.
     *
     * @return a DataDisplay object with the data to be displayed.
     */
    public DataDisplay displayData();

    /**
     * Display a limited set of data. The implementation of this method should handle the logic of what limited data is to be displayed.
     *
     * @return a DataDisplay object with the limited data to be displayed.
     */
    public DataDisplay limitedDisplayData();
}
