//
// Created by Joel Kanyi on 27/08/2023.
// Copyright (c) 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct AddTaskScreen: View {
    @State private var selectedDate = Date()
    @State private var showCancelButton: Bool = false
    @State private var taskType = "Other"
    let taskTypes = ["Work", "School", "Person", "Other"]
    @State private var focusSession: Int = 0

    var body: some View {
        NavigationView {
            ScrollView {
                Group {
                    VStack {
                        Text("Add Task")
                                .frame(maxWidth: UIScreen.main.bounds.width, alignment: .leading)
                                .font(.system(size: 12, weight: .semibold, design: .rounded))

                        HStack {
                            TextField(
                                    "Enter task Name",
                                    text: .constant("")
                            ).autocapitalization(.none)
                                    .disableAutocorrection(true)
                                    .keyboardType(.alphabet)
                                    .frame(maxWidth: UIScreen.main.bounds.width, maxHeight: 60, alignment: .leading)
                        }
                                .padding()
                                .overlay(RoundedRectangle(cornerRadius: 12)
                                        .stroke(Color.secondary, lineWidth: 1)
                                        .foregroundColor(.clear))
                    }
                }
                Spacer()
                        .frame(height: 16)
                Group {
                    VStack {
                        Text("Task Description")
                                .frame(maxWidth: UIScreen.main.bounds.width, alignment: .leading)
                                .font(.system(size: 12, weight: .semibold, design: .rounded))
                        HStack {
                            TextField(
                                    "Enter description",
                                    text: .constant("")
                            ).autocapitalization(.none)
                                    .disableAutocorrection(true)
                                    .keyboardType(.alphabet)
                                    .frame(maxWidth: UIScreen.main.bounds.width, maxHeight: 60, alignment: .leading)
                        }
                                .padding()
                                .overlay(RoundedRectangle(cornerRadius: 8)
                                        .stroke(Color.secondary, lineWidth: 0.5)
                                        .foregroundColor(.clear))
                    }

                    Spacer()
                            .frame(height: 16)
                    Group {
                        VStack {
                            Text("Task Date")
                                    .frame(maxWidth: UIScreen.main.bounds.width, alignment: .leading)
                                    .font(.system(size: 12, weight: .semibold, design: .rounded))

                            HStack {
                                DatePicker("Enter date", selection: $selectedDate, displayedComponents: .date)
                                        .frame(maxWidth: UIScreen.main.bounds.width, maxHeight: 30, alignment: .center)
                            }
                                    .padding()
                                    .overlay(RoundedRectangle(cornerRadius: 12)
                                            .stroke(Color.secondary, lineWidth: 1)
                                            .foregroundColor(.clear))
                        }
                    }
                    Spacer()
                            .frame(height: 16)

                    Group {
                        HStack {
                            Text("Task Type")
                                    .frame(maxWidth: UIScreen.main.bounds.width, alignment: .leading)
                                    .font(.system(size: 16, weight: .regular, design: .rounded))
                            Picker("Select task type", selection: $taskType) {
                                ForEach(taskTypes, id: \.self) {
                                    Text($0)
                                }
                            }
                                    .pickerStyle(.menu)
                        }
                                .frame(maxWidth: UIScreen.main.bounds.width, maxHeight: 30, alignment: .center)
                    }
                    Spacer()
                            .frame(height: 24)
                    Group {
                        VStack {
                            Text("Focus Sessions")
                                    .frame(maxWidth: UIScreen.main.bounds.width, alignment: .center)
                                    .font(.system(size: 16, weight: .regular, design: .rounded))

                            Spacer()
                                    .frame(height: 12)
                            HStack {
                                CircleButton(icon: "minus.circle") {
                                    print("Remove focus session")
                                    if focusSession > 0 {
                                        focusSession -= 1
                                    }
                                }
                                Text("\(focusSession)")
                                        .font(.system(size: 24))
                                CircleButton(icon: "plus.circle") {
                                    print("Add focus session")
                                    focusSession += 1
                                }
                            }
                        }
                    }
                }
                        .navigationBarTitle("Add Task", displayMode: .inline)
                        .navigationBarItems(
                                /*leading: Button(action: {
                                showCancelButton.toggle()
                            }) {
                                Text("Cancel").alert(isPresented: $showCancelButton) {
                                    Alert(title: Text("Are you sure you want to cancel?"), message: Text("You will lose all the data you have entered"), primaryButton: .destructive(Text("Yes"), action: {
                                        print("Yes")
                                    }), secondaryButton: .cancel(Text("No"), action: {
                                        print("No")
                                    }))
                                }
                            },*/
                                trailing: Button(action: {
                                    print("Save button tapped")
                                }) {
                                    Text("Save")
                                }
                        )
            }
                    .padding()
        }
    }

    struct CircleButton: View {
        let icon: String
        let onTap: () -> Void
        var body: some View {
            Button(action: {
                onTap()
            }) {
                Image(systemName: icon)
                        .font(.system(size: 32))
            }
        }
    }
}