package com.example.wayfare.Models;

import java.util.List;

public record UpcomingPastBookingResponse(List<BookingModel> upcoming, List<BookingModel> past){};