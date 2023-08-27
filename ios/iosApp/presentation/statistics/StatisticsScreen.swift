//
// Created by Joel Kanyi on 27/08/2023.
// Copyright (c) 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct StatisticsScreen: View {
    let statisticsViewmodel = DiModule.statisticsViewModel()
    var body: some View {
        VStack {
            Text(statisticsViewmodel.getStatistics())
        }
    }
}