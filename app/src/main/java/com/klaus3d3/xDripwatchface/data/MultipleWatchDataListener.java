package com.klaus3d3.xDripwatchface.data;

import java.util.List;

/**
 * Allows multiple registrations.
 */
public interface MultipleWatchDataListener {

    List<DataType> getDataTypes();

    void onDataUpdate(DataType type, Object value);
}
