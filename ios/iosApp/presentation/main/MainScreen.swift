//
// Created by Joel Kanyi on 27/08/2023.
// Copyright (c) 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct MainScreen: View {
    @State var selectedTab: Int = 0
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeScreen()
                    .tabItem {
                        Image(systemName: "house")
                    }
                    .tag(0)
            CalendarScreen()
                    .tabItem {
                        Image(systemName: "calendar")
                    }
                    .tag(1)
            /**
             * Add a circle button for adding tasks
             */
            AddTaskScreen()
                    .tabItem {
                        Image(systemName: "plus.circle")
                                /**
                         *Increase the size of the circle button
                                 */
                                .resizable()
                                .imageScale(.large)
                                .font(.system(size: 100))
                    }
                    .tag(2)


            StatisticsScreen()
                    .tabItem {
                        Image(systemName: "chart.bar")
                    }
                    .tag(3)
            SettingsScreen()
                    .tabItem {
                        Image(systemName: "gear")
                    }
                    .tag(4)
        }

                .accentColor(Color("Main"))
                .navigationBarBackButtonHidden()
                .navigationBarHidden(true)
                .onAppear {
                    // correct the transparency bug for Tab bars
                    let tabBarAppearance = UITabBarAppearance()
                    tabBarAppearance.configureWithOpaqueBackground()
                    if #available(iOS 15.0, *) {
                        UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
                    } else {
                        // Fallback on earlier versions
                    }
                    // correct the transparency bug for Navigation bars
                    let navigationBarAppearance = UINavigationBarAppearance()
                    navigationBarAppearance.configureWithOpaqueBackground()
                    UINavigationBar.appearance().scrollEdgeAppearance = navigationBarAppearance
                }
    }
}