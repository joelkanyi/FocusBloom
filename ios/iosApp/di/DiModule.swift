//
//  AppModule.swift
//  ios
//
//  Created by Harshith Shetty on 03/05/22.
//  Copyright © 2022 Harshith Shetty. All rights reserved.
//

import Foundation
import shared

class DiModule {
    static var koin = { InitKoin().invoke() }()

    static func homeViewModel() -> HomeViewModel {
        return DIAccessorKt.getHomeViewModel(koin: DiModule.koin)
    }

    static func settingsViewModel() -> SettingsViewModel {
        return DIAccessorKt.getSettingsViewModel(koin: DiModule.koin)
    }

    static func calendarViewModel() -> CalendarViewModel {
        return DIAccessorKt.getCalendarViewModel(koin: DiModule.koin)
    }

    static func statisticsViewModel() -> StatisticsViewModel {
        return DIAccessorKt.getStatisticsViewModel(koin: DiModule.koin)
    }
}
