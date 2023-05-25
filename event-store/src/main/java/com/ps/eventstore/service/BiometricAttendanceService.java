package com.ps.eventstore.service;

import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;

public interface BiometricAttendanceService {

    BiometricAttendance swipeInAndOut(BiometricAttendance biometricAttendance);
}
