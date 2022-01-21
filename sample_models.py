from keras import backend as K
from keras.models import Model
from keras.layers import (BatchNormalization, Conv1D, Dense, Input, 
    TimeDistributed, Activation, Bidirectional, SimpleRNN, GRU, LSTM)

def cnn_rnn_model(input_dim, filters, kernel_size, conv_stride,
    conv_border_mode, units, output_dim=29):

    input_data = Input(name='the_input', shape=(None, input_dim))

    conv_1d = Conv1D(filters, kernel_size, 
                     strides=conv_stride, 
                     padding=conv_border_mode,
                     activation='relu',
                     name='conv1d')(input_data)

    bn_cnn = BatchNormalization(name='bn_conv_1d')(conv_1d)

    simp_rnn = GRU(units, activation='relu',
        return_sequences=True, implementation=2, name='rnn')(bn_cnn)
   
    bn_rnn = BatchNormalization(name='bn_rnn_1d')(simp_rnn)
  
    time_dense = TimeDistributed(Dense(output_dim))(bn_rnn)
 
    y_pred = Activation('softmax', name='softmax')(time_dense)
 
    model = Model(inputs=input_data, outputs=y_pred)
    model.output_length = lambda x: cnn_output_length(
        x, kernel_size, conv_border_mode, conv_stride)
    print(model.summary())
    return model

def cnn_output_length(input_length, filter_size, border_mode, stride,
                       dilation=1):
   
    if input_length is None:
        return None
    assert border_mode in {'same', 'valid'}
    dilated_filter_size = filter_size + (filter_size - 1) * (dilation - 1)
    if border_mode == 'same':
        output_length = input_length
    elif border_mode == 'valid':
        output_length = input_length - dilated_filter_size + 1
    return (output_length + stride - 1) // stride


def final_model(input_dim, filters, kernel_size, conv_stride,
    conv_border_mode, units, output_dim=29, drop_out_rate=0.5, number_of_layers=2):

    input_data = Input(name='the_input', shape=(None, input_dim))
    print("OOOOOOOOOOOOOOOOOOOOO ",input_dim)
    conv_1d = Conv1D(filters, kernel_size, 
                     strides=conv_stride, 
                     padding=conv_border_mode,
                     activation='relu',
                     name='conv1d')(input_data)

    bn_cnn = BatchNormalization(name='bn_conv_1d')(conv_1d)
    rnn=GRU(output_dim, return_sequences=True, name='rnn', dropout=drop_out_rate, activation='relu')
    bidir_rnn = Bidirectional(rnn)(bn_cnn)

    time_dense = TimeDistributed(Dense(output_dim))(bidir_rnn)

    y_pred = Activation('softmax', name='softmax')(time_dense)
  
    model = Model(inputs=input_data, outputs=y_pred)
    model.output_length = lambda x: cnn_output_length(
        x, kernel_size, conv_border_mode, conv_stride)
    print(model.summary())
    return model